package com.jingyuyao.tactical.model.state;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class StateModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapState.class);
        bind(State.class).annotatedWith(MapState.InitialState.class).to(Waiting.class).in(Singleton.class);
    }
}
