package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.model.Level;

public class LevelScreenFactory {
    private final TacticalAdventure game;
    private final MapViewFactory mapViewFactory;
    private final MapUIFactory mapUIFactory;

    public LevelScreenFactory(TacticalAdventure game, AssetManager assetManager) {
        this.game = game;
        mapViewFactory = new MapViewFactory(assetManager);
        mapUIFactory = new MapUIFactory(assetManager);
    }

    public LevelScreen createScreen(Level level, TiledMap tiledMap) {
        MapView mapView = mapViewFactory.create(tiledMap, level);
        MapUI mapUI = mapUIFactory.create(level);
        return new LevelScreen(game, level, mapView, mapUI);
    }
}
