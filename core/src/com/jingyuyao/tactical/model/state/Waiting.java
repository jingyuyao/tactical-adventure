package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.mark.Markings;

import javax.inject.Inject;

public class Waiting extends AbstractState {
  @Inject
  Waiting(
      EventBus eventBus, MapState mapState, Markings markings, StateFactory stateFactory) {
    super(eventBus, mapState, markings, stateFactory);
  }

  @Override
  public void select(Player player) {
    if (player.isActionable()) {
      goTo(getStateFactory().createMoving(player));
    }
  }

  @Override
  public void select(Enemy enemy) {
    getMarkings().toggleDangerArea(enemy);
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
    }
  }
}
