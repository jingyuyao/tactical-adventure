package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.AsyncRunner;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;

public class Retaliating extends AbstractState {

  private final Characters characters;
  private final AsyncRunner asyncRunner;

  @Inject
  Retaliating(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Characters characters,
      AsyncRunner asyncRunner) {
    super(eventBus, mapState, stateFactory);
    this.characters = characters;
    this.asyncRunner = asyncRunner;
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
    for (Enemy enemy : Iterables.filter(characters, Enemy.class)) {
      asyncRunner.execute(enemy.getRetaliation());
    }
    asyncRunner.execute(new Runnable() {
      @Override
      public void run() {
        newWaitStack();
      }
    });
  }
}
