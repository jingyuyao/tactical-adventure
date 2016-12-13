package com.jingyuyao.tactical.map;

import com.google.inject.AbstractModule;

public class MapModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapFactory.class);
        bind(TerrainFactory.class);
    }
}
