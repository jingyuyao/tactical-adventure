package com.jingyuyao.tactical.view.actor;

import com.google.inject.AbstractModule;

public class ActorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ActorFactory.class);
    }
}
