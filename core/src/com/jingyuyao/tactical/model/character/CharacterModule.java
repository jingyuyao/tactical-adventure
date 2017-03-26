package com.jingyuyao.tactical.model.character;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.world.Movements;

public class CharacterModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Battle.class);
    requireBinding(Movements.class);
  }
}
