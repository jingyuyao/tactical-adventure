package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.MapObject;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MapViewFactory.class).in(Singleton.class);
        bind(MapActorFactory.class).in(Singleton.class);
        bind(ShapeRenderer.class).in(Singleton.class);
    }

    // Remember to clear me when changing map view
    @Provides
    @Singleton
    Map<MapObject, Actor> provideMapObjectActorMap() {
        return new HashMap<MapObject, Actor>();
    }
}
