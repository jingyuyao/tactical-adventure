package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Injectable factory for controllers.
 */
public class ControllerFactory {

    public InputProcessor createMapController(Stage stage, int worldWidth, int worldHeight) {
        MapMovementController mapMovementController = new MapMovementController(stage, worldWidth, worldHeight);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mapMovementController);
        inputMultiplexer.addProcessor(stage);
        return inputMultiplexer;
    }
}
