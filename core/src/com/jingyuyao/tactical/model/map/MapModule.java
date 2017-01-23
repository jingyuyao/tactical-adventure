package com.jingyuyao.tactical.model.map;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.EventSubscriber;
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

    Multibinder<EventSubscriber> subscriberBinder =
        Multibinder.newSetBinder(binder(), EventSubscriber.class);
    subscriberBinder.addBinding().to(Characters.class);
    subscriberBinder.addBinding().to(Terrains.class);
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
