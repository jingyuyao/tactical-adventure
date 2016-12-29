package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Level;

public class LevelScreenFactory {
    private final MapViewFactory mapViewFactory;
    private final MapUIFactory mapUIFactory;

    public LevelScreenFactory(EventBus eventBus, AssetManager assetManager) {
        mapViewFactory = new MapViewFactory(eventBus, assetManager);
        mapUIFactory = new MapUIFactory(eventBus, assetManager);
    }

    public LevelScreen createScreen(Level level, TiledMap tiledMap) {
        MapView mapView = mapViewFactory.create(tiledMap, level);
        MapUI mapUI = mapUIFactory.create(level);
        return new LevelScreen(level, mapView, mapUI);
    }
}
