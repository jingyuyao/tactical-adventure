package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.data.LevelLoader;
import com.jingyuyao.tactical.model.Level;

public class LevelScreenFactory {
    private final TacticalAdventure game;
    private final AssetManager assetManager;
    private final MapViewFactory mapViewFactory;
    private final MapUIFactory mapUIFactory;

    public LevelScreenFactory(TacticalAdventure game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        mapViewFactory = new MapViewFactory(assetManager);
        mapUIFactory = new MapUIFactory(assetManager);
    }

    public LevelScreen create(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        Level level = LevelLoader.createLevel(tiledMap);
        MapView mapView = mapViewFactory.create(tiledMap, level.getMap(), level.getMapState());
        MapUI mapUI = mapUIFactory.create(level.getMap(), level.getMapState());
        return new LevelScreen(game, level, mapView, mapUI);
    }
}
