package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapViewFactory.class).in(Singleton.class);
        bind(MapActorFactory.class).in(Singleton.class);
        bind(ShapeRenderer.class).in(Singleton.class);
    }
}
