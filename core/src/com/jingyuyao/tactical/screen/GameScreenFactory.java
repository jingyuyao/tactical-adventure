package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.data.MapLoader;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.view.MapUI;
import com.jingyuyao.tactical.view.MapUIFactory;
import com.jingyuyao.tactical.view.MapView;
import com.jingyuyao.tactical.view.MapViewFactory;

public class GameScreenFactory {
    private final TacticalAdventure game;
    private final AssetManager assetManager;
    private final MapViewFactory mapViewFactory;
    private final MapUIFactory mapUIFactory;

    public GameScreenFactory(TacticalAdventure game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        mapViewFactory = new MapViewFactory(assetManager);
        mapUIFactory = new MapUIFactory(assetManager);
    }

    public GameScreen create(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        Map map = MapLoader.create(tiledMap);
        MapState mapState = new MapState(map);
        MapView mapView = mapViewFactory.create(tiledMap, map, mapState);
        MapUI mapUI = mapUIFactory.create(map, mapState);
        MapController mapController =
                new MapController(mapUI.getUi(), mapView.getWorld(), map.getWidth(), map.getHeight());
        return new GameScreen(game, mapView, mapUI, mapController);
    }
}
