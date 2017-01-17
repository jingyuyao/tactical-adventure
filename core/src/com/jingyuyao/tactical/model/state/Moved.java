package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import javax.inject.Inject;

class Moved extends AbstractPlayerState {

  @Inject
  Moved(EventBus eventBus, MapState mapState, StateFactory stateFactory, @Assisted Player player) {
    super(eventBus, mapState, stateFactory, player);
  }
}
