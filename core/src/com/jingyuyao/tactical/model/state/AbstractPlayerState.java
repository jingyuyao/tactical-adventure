package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;

abstract class AbstractPlayerState extends BaseState implements PlayerState {

  private final StateFactory stateFactory;
  private final Player player;

  AbstractPlayerState(EventBus eventBus, WorldState worldState, StateFactory stateFactory,
      Player player) {
    super(eventBus, worldState);
    this.stateFactory = stateFactory;
    this.player = player;
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  StateFactory getStateFactory() {
    return stateFactory;
  }

  void finish() {
    player.setActionable(false);
    branchTo(stateFactory.createWaiting());
  }
}
