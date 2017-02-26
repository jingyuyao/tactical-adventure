package com.jingyuyao.tactical.model.character;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Terrains;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

public class CharacterModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Battle.class);
    requireBinding(Movements.class);
    requireBinding(Terrains.class);
  }

  @Provides
  @CharacterEventBus
  EventBus provideCharacterEventBus() {
    return new EventBus("character");
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface CharacterEventBus {

  }
}
