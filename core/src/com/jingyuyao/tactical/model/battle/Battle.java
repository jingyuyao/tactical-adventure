package com.jingyuyao.tactical.model.battle;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Battle {

  private final EventBus eventBus;

  @Inject
  Battle(@ModelEventBus EventBus eventBus) {
    this.eventBus = eventBus;
  }
}
