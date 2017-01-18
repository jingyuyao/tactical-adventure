package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.MovementFactory;
import javax.inject.Inject;

public class Waiting extends AbstractState {

  private final MovementFactory movementFactory;

  @Inject
  Waiting(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      MovementFactory movementFactory) {
    super(eventBus, mapState, stateFactory);
    this.movementFactory = movementFactory;
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
      post(this);
      goTo(getStateFactory().createRetaliating());
    }
  }
}
