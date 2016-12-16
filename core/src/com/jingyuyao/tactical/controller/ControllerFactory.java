package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.MapView;

/**
 * Injectable factory for controllers.
 */
public class ControllerFactory {

    public InputProcessor createMapController(MapView mapView) {
        Stage stage = mapView.getStage();
        MapMovementController mapMovementController =
                new MapMovementController(stage, mapView.getWorldWidth(), mapView.getWorldHeight());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mapMovementController);
        inputMultiplexer.addProcessor(mapView.getStage());
        return inputMultiplexer;
    }
}
