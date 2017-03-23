package com.jingyuyao.tactical.model.battle;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.World;

public class BattleModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(World.class);
    requireBinding(EventBus.class);
  }
}
