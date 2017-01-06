package com.jingyuyao.tactical.model.map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.mark.Marker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Singleton;

public class MapModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(TerrainFactory.class));

    bind(Characters.class);
    bind(Terrains.class);
    bind(TargetsFactory.class);
  }

  @Provides
  @MapObject.InitialMarkers
  List<Marker> provideInitialMarkers() {
    return new ArrayList<Marker>();
  }

  @Provides
  @Singleton
  @Characters.BackingCharacterSet
  Set<Character> provideBackingCharacterSet() {
    return new HashSet<Character>();
  }

  @Provides
  @Singleton
  @Terrains.BackingTerrainTable
  Table<Integer, Integer, Terrain> provideBackingTerrainTable() {
    return HashBasedTable.create();
  }
}
