package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.mark.Markings;

abstract class AbstractPlayerState extends AbstractState {

  private final Player player;

  AbstractPlayerState(
      EventBus eventBus,
      MapState mapState,
      Markings markings,
      StateFactory stateFactory,
      Player player) {
    super(eventBus, mapState, markings, stateFactory);
    this.player = player;
  }

  @Override
  public void exit() {
    getMarkings().clearPlayerMarking();
    super.exit();
  }

  Player getPlayer() {
    return player;
  }
}
