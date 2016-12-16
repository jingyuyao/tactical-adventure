package com.jingyuyao.tactical.model;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ModelModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapFactory.class).in(Singleton.class);
        bind(TerrainFactory.class).in(Singleton.class);
    }
}
