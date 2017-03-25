package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
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
    retaliate(world.getCells().toList(), 0);
  }

  private void retaliate(final ImmutableList<Cell> cells, final int i) {
    if (i == cells.size()) {
      branchTo(stateFactory.createWaiting());
      return;
    }

    Cell cell = cells.get(i);
    if (!cell.hasEnemy()) {
      retaliate(cells, i + 1);
      return;
    }

    Enemy enemy = cell.getEnemy();
    post(new ActivatedEnemy(enemy));
    Futures.addCallback(enemy.retaliate(cell), new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        retaliate(cells, i + 1);
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }
}
