package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.DeactivatedEnemy;
import com.jingyuyao.tactical.model.map.Characters;
import javax.inject.Inject;

public class Retaliating extends BaseState {

  private final StateFactory stateFactory;
  private final Characters characters;

  @Inject
  Retaliating(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Characters characters) {
    super(eventBus, mapState);
    this.stateFactory = stateFactory;
    this.characters = characters;
  }

  @Override
  public void enter() {
    super.enter();
    retaliate();
  }

  private void retaliate() {
    // TODO: does order matter?
    ListenableFuture<Void> currentRetaliation = Futures.immediateFuture(null);
    for (final Enemy enemy : characters.fluent().filter(Enemy.class)) {
      // Make enemies retaliate one at a time
      currentRetaliation =
          Futures.transformAsync(currentRetaliation, new AsyncFunction<Void, Void>() {
            @Override
            public ListenableFuture<Void> apply(Void input) throws Exception {
              getEventBus().post(new ActivatedEnemy(enemy));
              return enemy.retaliate();
            }
          });
    }
    Futures.addCallback(currentRetaliation, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        getEventBus().post(new DeactivatedEnemy());
        branchTo(stateFactory.createWaiting());
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }
}
