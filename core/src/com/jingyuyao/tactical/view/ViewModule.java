package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.controller.HighlightListener;

import javax.inject.Singleton;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapViewFactory.class).in(Singleton.class);
        bind(TerrainFactory.class).in(Singleton.class);
        bind(CharacterFactory.class).in(Singleton.class);
        bind(ShapeRenderer.class).in(Singleton.class);
        bind(Highlighter.class);
        bind(HighlightListener.class);
    }
}
