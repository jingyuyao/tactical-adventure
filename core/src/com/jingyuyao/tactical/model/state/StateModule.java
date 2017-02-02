package com.jingyuyao.tactical.model.state;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.map.Movements;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Deque;
import java.util.LinkedList;
import javax.inject.Singleton;

public class StateModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Battle.class);
    requireBinding(Movements.class);

    install(new FactoryModuleBuilder().build(StateFactory.class));
  }

  @Provides
  @Singleton
  @BackingStateStack
  Deque<State> provideStateStack() {
    return new LinkedList<>();
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingStateStack {

  }
}
