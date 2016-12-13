package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class MapModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapFactory.class).in(Singleton.class);
        bind(TerrainFactory.class).in(Singleton.class);
        bind(ShapeRenderer.class).in(Singleton.class);
        bind(HighlightRenderer.class).in(Singleton.class);
    }
}
