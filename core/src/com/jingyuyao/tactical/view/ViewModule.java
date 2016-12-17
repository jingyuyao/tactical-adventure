package com.jingyuyao.tactical.view;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapViewFactory.class).in(Singleton.class);
        bind(MapActorFactory.class).in(Singleton.class);
    }
}
