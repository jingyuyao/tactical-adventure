package com.jingyuyao.tactical.model.battle;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;

public class BattleModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Characters.class);
    requireBinding(Terrains.class);

    bind(PiercingFactory.class);
  }
}
