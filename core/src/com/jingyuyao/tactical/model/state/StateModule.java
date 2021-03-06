package com.jingyuyao.tactical.model.state;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.world.World;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Deque;
import java.util.LinkedList;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class StateModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(ModelBus.class);
    requireBinding(World.class);

    install(new FactoryModuleBuilder().build(StateFactory.class));
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
