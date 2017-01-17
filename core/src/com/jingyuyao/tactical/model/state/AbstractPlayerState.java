package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;

abstract class AbstractPlayerState extends AbstractState {

  private final Player player;

  AbstractPlayerState(
      EventBus eventBus, MapState mapState, StateFactory stateFactory, Player player) {
    super(eventBus, mapState, stateFactory);
    this.player = player;
  }

  Player getPlayer() {
    return player;
  }
}
