package com.jingyuyao.tactical.view;

import com.badlogic.gdx.maps.tiled.TiledMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelScreenFactory {
    private final MapViewFactory mapViewFactory;
    private final MapUI mapUI;

    @Inject
    LevelScreenFactory(MapViewFactory mapViewFactory, MapUI mapUI) {
        this.mapViewFactory = mapViewFactory;
        this.mapUI = mapUI;
    }

    public LevelScreen createScreen(TiledMap tiledMap) {
        MapView mapView = mapViewFactory.create(tiledMap);
        return new LevelScreen(mapView, mapUI);
    }
}
