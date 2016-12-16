package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.MapObject;

/**
 * Injectable factory for controllers.
 */
public class ControllerFactory {

    public InputProcessor createMapController(Stage stage, int worldWidth, int worldHeight) {
        CameraController cameraController = new CameraController(stage, worldWidth, worldHeight);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(cameraController);
        inputMultiplexer.addProcessor(stage);
        return inputMultiplexer;
    }

    public HighlightController createHighlightController(MapObject mapObject) {
        return new HighlightController(mapObject);
    }

    public CharacterController characterController(Character character) {
        return new CharacterController(character);
    }
}
