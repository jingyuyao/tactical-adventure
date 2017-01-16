package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import javax.inject.Inject;

public class Waiting extends AbstractState {

  @Inject
  Waiting(EventBus eventBus, MapState mapState, StateFactory stateFactory) {
    super(eventBus, mapState, stateFactory);
  }

  @Override
  public void select(Player player) {
    if (player.isActionable()) {
      goTo(getStateFactory().createMoving(player));
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
