package com.jingyuyao.tactical.model.battle;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.ModelBus;

public class BattleModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(ModelBus.class);
  }
}
