package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapFactory;
import com.jingyuyao.tactical.view.MapView;
import com.jingyuyao.tactical.view.MapViewFactory;

import javax.inject.Inject;

public class GameScreenFactory {
    private final TacticalAdventure game;
    private final AssetManager assetManager;
    private final MapFactory mapFactory;
    private final MapViewFactory mapViewFactory;
    private final ControllerFactory controllerFactory;

    @Inject
    public GameScreenFactory(TacticalAdventure game, AssetManager assetManager, MapFactory mapFactory,
                             MapViewFactory mapViewFactory, ControllerFactory controllerFactory) {
        this.game = game;
        this.assetManager = assetManager;
        this.mapFactory = mapFactory;
        this.mapViewFactory = mapViewFactory;
        this.controllerFactory = controllerFactory;
    }

    public GameScreen create(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        Map map = mapFactory.create(tiledMap);
        MapView mapView = mapViewFactory.create(tiledMap, map);
        InputProcessor mapController =
                controllerFactory.createMapController(mapView.getStage(), map.getWorldWidth(), map.getWorldHeight());
        return new GameScreen(game, mapView, mapController);
    }
}
