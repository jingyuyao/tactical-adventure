package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;

abstract class AbstractPlayerState extends BaseState implements PlayerState {

  private final StateFactory stateFactory;
  private final Player player;

  AbstractPlayerState(
      ModelBus modelBus, WorldState worldState, StateFactory stateFactory, Player player) {
    super(modelBus, worldState);
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
