package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapFactory;
import com.jingyuyao.tactical.view.MapView;
import com.jingyuyao.tactical.view.MapViewFactory;

import javax.inject.Inject;

public class GameScreenFactory {
    private final TacticalAdventure game;
    private final AssetManager assetManager;
    private final MapViewFactory mapViewFactory;

    @Inject
    public GameScreenFactory(TacticalAdventure game, AssetManager assetManager, MapViewFactory mapViewFactory) {
        this.game = game;
        this.assetManager = assetManager;
        this.mapViewFactory = mapViewFactory;
    }

    public GameScreen create(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        Map map = MapFactory.create(tiledMap);
        MapView mapView = mapViewFactory.create(tiledMap, map);
        MapController mapController = new MapController(mapView.getStage(), map.getWorldWidth(), map.getWorldHeight());
        return new GameScreen(game, mapView, mapController);
    }
}
