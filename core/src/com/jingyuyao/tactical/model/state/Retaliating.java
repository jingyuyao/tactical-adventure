package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;

public class Retaliating extends AbstractState {

  private final Characters characters;

  @Inject
  Retaliating(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Characters characters) {
    super(eventBus, mapState, stateFactory);
    this.characters = characters;
  }

  @Override
  public void enter() {
    retaliate();
  }

  @Override
  public void select(Player player) {
  }

  @Override
  public void select(Enemy enemy) {
  }

  @Override
  public void select(Terrain terrain) {
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of();
  }

  private void retaliate() {
    // TODO: does order matter?
    ListenableFuture<Void> currentRetaliation = Futures.immediateFuture(null);
    for (final Enemy enemy : Iterables.filter(characters, Enemy.class)) {
      // Make enemies retialiate one at a time
      currentRetaliation =
          Futures.transformAsync(currentRetaliation, new AsyncFunction<Void, Void>() {
            @Override
            public ListenableFuture<Void> apply(Void input) throws Exception {
              return enemy.retaliate();
            }
          });
    }
    Futures.addCallback(currentRetaliation, new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        newWaitStack();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }
}