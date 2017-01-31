package com.jingyuyao.tactical.model.character;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movements;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class CharacterModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Movements.class);
    requireBinding(Characters.class);
  }

  @Provides
  @CharacterEventBus
  EventBus provideCharacterEventBus() {
    return new EventBus("character");
  }

  @Provides
  @InitialItems
  List<Item> provideInitialItems() {
    return new ArrayList<Item>();
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
