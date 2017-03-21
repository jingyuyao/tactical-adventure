package com.jingyuyao.tactical.model.state;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movements;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Deque;
import java.util.LinkedList;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class StateModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(EventBus.class);
    requireBinding(Battle.class);
    requireBinding(Movements.class);
    requireBinding(Characters.class);

    install(new FactoryModuleBuilder().build(StateFactory.class));
    bind(SelectionHandler.class).to(MapState.class);
  }

  @Provides
  @Singleton
  @BackingStateStack
  Deque<State> provideStateStack() {
    return new LinkedList<>();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingStateStack {

  }
}
