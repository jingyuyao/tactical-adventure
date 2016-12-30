package com.jingyuyao.tactical.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.model.Level;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelScreenFactory {
    private final MapViewFactory mapViewFactory;
    private final MapUIFactory mapUIFactory;

    @Inject
    public LevelScreenFactory(MapViewFactory mapViewFactory, MapUIFactory mapUIFactory) {
        this.mapViewFactory = mapViewFactory;
        this.mapUIFactory = mapUIFactory;
    }

    public LevelScreen createScreen(Level level, TiledMap tiledMap) {
        MapView mapView = mapViewFactory.create(tiledMap, level);
        MapUI mapUI = mapUIFactory.create(level);
        return new LevelScreen(mapView, mapUI);
    }
}
