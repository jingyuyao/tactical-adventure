package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Terrain;

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

    public HighlightController createHighlightController(Map map, MapObject mapObject) {
        return new HighlightController(map, mapObject);
    }

    public CharacterController characterController(Map map, Character character) {
        return new CharacterController(map, character);
    }

    public TerrainController terrainController(Map map, Terrain terrain) {
        return new TerrainController(map, terrain);
    }
}
