package com.jingyuyao.tactical.model.state;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import javax.inject.Singleton;
import java.util.Deque;
import java.util.LinkedList;

public class StateModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(StateFactory.class));
    bind(MapState.class);
  }

  @Provides
  @Singleton
  @MapState.BackingStateStack
  Deque<State> provideStateStack() {
    return new LinkedList<State>();
  }
}
