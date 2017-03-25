package com.jingyuyao.tactical.model.battle;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class BattleModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(EventBus.class);
  }
}
