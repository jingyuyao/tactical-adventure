package com.jingyuyao.tactical;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

class GameModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);
    }
}
