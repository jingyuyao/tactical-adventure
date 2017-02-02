package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.character.Player;

abstract class AbstractPlayerState extends AbstractState {

  private final Player player;

  AbstractPlayerState(MapState mapState, StateFactory stateFactory, Player player) {
    super(mapState, stateFactory);
    this.player = player;
  }

  Player getPlayer() {
    return player;
  }

  void finish() {
    player.setActionable(false);
    branchToWait();
  }
}
