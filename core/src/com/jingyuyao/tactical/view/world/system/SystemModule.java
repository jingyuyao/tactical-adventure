package com.jingyuyao.tactical.view.world.system;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.Moving;
import com.jingyuyao.tactical.view.world.component.PlayerComponent;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class SystemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Engine.class);
    requireBinding(new Key<ComponentMapper<Position>>() {
    });
    requireBinding(new Key<ComponentMapper<Frame>>() {
    });
    requireBinding(new Key<ComponentMapper<LoopAnimation>>() {
    });
    requireBinding(new Key<ComponentMapper<SingleAnimation>>() {
    });
    requireBinding(new Key<ComponentMapper<Moving>>() {
    });
    requireBinding(new Key<ComponentMapper<PlayerComponent>>() {
    });
  }

  @Provides
  @Singleton
  @CharacterEntityMap
  Map<Character, Entity> provideCharacterEntities() {
    return new HashMap<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface CharacterEntityMap {

  }
}
