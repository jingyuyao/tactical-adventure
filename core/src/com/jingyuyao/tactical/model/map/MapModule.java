package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class MapModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Movements.class);

    install(new FactoryModuleBuilder().build(MovementFactory.class));
  }

  @Provides
  @Singleton
  @BackingCharacterSet
  Set<Character> provideBackingCharacterSet() {
    return new HashSet<>();
  }

  @Provides
  @Singleton
  @BackingTerrainMap
  Map<Coordinate, Terrain> provideBackingTerrainMap() {
    return new HashMap<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingCharacterSet {

  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingTerrainMap {

  }
}
