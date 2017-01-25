package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MovementFactory;
import javax.inject.Inject;

public class Waiting extends AbstractState {

  private final MovementFactory movementFactory;
  private final Characters characters;

  @Inject
  Waiting(
      MapState mapState,
      StateFactory stateFactory,
      MovementFactory movementFactory,
      Characters characters) {
    super(mapState, stateFactory);
    this.movementFactory = movementFactory;
    this.characters = characters;
  }

  @Override
  public void select(Player player) {
    if (player.isActionable()) {
      goTo(getStateFactory().createMoving(player, movementFactory.create(player)));
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(this.new EndTurn());
  }

  public class EndTurn implements Action {

    @Override
    public String getName() {
      return "end";
    }

    @Override
    public void run() {
      for (Player player : Iterables.filter(characters, Player.class)) {
        player.setActionable(true);
      }
      goTo(getStateFactory().createRetaliating());
    }
  }
}
