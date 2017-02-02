package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

public class Waiting extends AbstractState {

  private final Characters characters;
  private final Movements movements;

  @Inject
  Waiting(
      MapState mapState,
      StateFactory stateFactory,
      Characters characters,
      Movements movements) {
    super(mapState, stateFactory);
    this.characters = characters;
    this.movements = movements;
  }

  @Override
  public void select(Player player) {
    if (player.isActionable()) {
      goTo(getStateFactory().createMoving(player, movements.distanceFrom(player)));
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new EndTurnAction(this));
  }

  void endTurn() {
    for (Player player : characters.fluent().filter(Player.class)) {
      player.setActionable(true);
    }
    goTo(getStateFactory().createRetaliating());
  }
}
