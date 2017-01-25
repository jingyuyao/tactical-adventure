package com.jingyuyao.tactical.model.state;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.map.MovementFactory;
import java.util.Deque;
import java.util.LinkedList;
import javax.inject.Singleton;

public class StateModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(MovementFactory.class);

    install(new FactoryModuleBuilder().build(StateFactory.class));
  }

  @Provides
  @Singleton
  @MapState.BackingStateStack
  Deque<State> provideStateStack() {
    return new LinkedList<State>();
  }
}
