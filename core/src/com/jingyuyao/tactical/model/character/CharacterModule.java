package com.jingyuyao.tactical.model.character;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class CharacterModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(TerrainGraphs.class);
    requireBinding(Characters.class);
    requireBinding(MovementFactory.class);

    install(new FactoryModuleBuilder().build(CharacterFactory.class));
  }

  @Provides
  @InitialItems
  List<Item> provideInitialItems() {
    return new ArrayList<Item>();
  }

  @Provides
  @CharacterEventBus
  EventBus provideCharacterEventBus() {
    return new EventBus("character");
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface CharacterEventBus {

  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface InitialItems {

  }
}
