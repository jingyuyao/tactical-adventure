package com.jingyuyao.tactical.model.state;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.map.Cell;
import javax.inject.Inject;

public class Retaliating extends BaseState {

  private final StateFactory stateFactory;
  private final World world;

  @Inject
  Retaliating(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      World world) {
    super(eventBus, mapState);
    this.stateFactory = stateFactory;
    this.world = world;
  }

  @Override
  public void enter() {
    super.enter();
    retaliate();
  }

  private void retaliate() {
    ImmutableList<Cell> enemyCells = world.getCells().filter(new Predicate<Cell>() {
      @Override
      public boolean apply(Cell input) {
        return input.hasEnemy();
      }
    }).toList();

    // TODO: does order matter?
    ListenableFuture<Void> currentRetaliation = Futures.immediateFuture(null);
    for (final Cell cell : enemyCells) {
      final Enemy enemy = cell.getEnemy();
      // Make enemies retaliate one at a time
      currentRetaliation =
          Futures.transformAsync(currentRetaliation, new AsyncFunction<Void, Void>() {
            @Override
            public ListenableFuture<Void> apply(Void input) throws Exception {
              post(new ActivatedEnemy(enemy));
              return enemy.retaliate(cell);
            }
          });
    }

    Futures.addCallback(currentRetaliation, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        branchTo(stateFactory.createWaiting());
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }
}
