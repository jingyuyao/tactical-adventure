package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

/**
 * A {@link State} that doesn't do anything. Useful as an intermediate state to wait for
 * animation to finish.
 */
public class IgnoreInput extends AbstractState {

  @Inject
  IgnoreInput(MapState mapState, StateFactory stateFactory) {
    super(mapState, stateFactory);
  }

  @Override
  public void exit() {
    // This state is temporary, don't keep it on the state stack
    removeLast();
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
}
