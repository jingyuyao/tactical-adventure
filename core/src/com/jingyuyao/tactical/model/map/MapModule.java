package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.common.Algorithms;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import com.jingyuyao.tactical.model.map.Terrains.BackingTerrainMap;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;

public class MapModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Algorithms.class);
    requireBinding(MarkingFactory.class);

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

  @Provides
  @InitialMarkers
  Multiset<Marker> provideMarkers() {
    return HashMultiset.create();
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public @interface InitialMarkers {

  }
}
