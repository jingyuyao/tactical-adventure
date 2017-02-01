package com.jingyuyao.tactical.model.battle;

import com.google.inject.AbstractModule;

public class BattleModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Battle.class);
  }
}
