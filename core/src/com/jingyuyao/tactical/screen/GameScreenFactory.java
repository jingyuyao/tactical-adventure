package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.google.inject.Inject;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.view.MapView;
import com.jingyuyao.tactical.view.MapViewFactory;

public class GameScreenFactory {
    private final TacticalAdventure game;
    private final AssetManager assetManager;
    private final MapViewFactory mapViewFactory;
    private final ControllerFactory controllerFactory;

    @Inject
    public GameScreenFactory(TacticalAdventure game, AssetManager assetManager, MapViewFactory mapViewFactory,
                             ControllerFactory controllerFactory) {
        this.game = game;
        this.assetManager = assetManager;
        this.mapViewFactory = mapViewFactory;
        this.controllerFactory = controllerFactory;
    }

    public GameScreen create(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        MapView mapView = mapViewFactory.create(tiledMap);
        InputProcessor mapController = controllerFactory.createMapController(mapView);
        return new GameScreen(game, mapView, mapController);
    }
}
