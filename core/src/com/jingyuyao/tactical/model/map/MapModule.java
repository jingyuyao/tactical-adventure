package com.jingyuyao.tactical.model.map;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Terrains.BackingTerrainMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;

public class MapModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(TerrainFactory.class));

    bind(Characters.class);
    bind(Terrains.class);
    bind(MovementFactory.class);
  }

  @Provides
  @Singleton
  @Characters.BackingCharacterSet
  Set<Character> provideBackingCharacterSet() {
    return new HashSet<Character>();
  }

  @Provides
  @Singleton
  @BackingTerrainMap
  Map<Coordinate, Terrain> provideBackingTerrainMap() {
    return new HashMap<Coordinate, Terrain>();
  }
}
