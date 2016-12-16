package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.AssetsModule;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.view.MapActor;

import javax.inject.Inject;

/**
 * Injectable factory for controllers.
 */
public class ControllerFactory {
    private final java.util.Map<MapObject, MapActor> actorMap;
    private final AssetManager assetManager;
    private final Sprite blueOverlay;

    @Inject
    public ControllerFactory(java.util.Map<MapObject, MapActor> actorMap, AssetManager assetManager) {
        this.actorMap = actorMap;
        this.assetManager = assetManager;
        blueOverlay = new Sprite(assetManager.get(AssetsModule.BLUE_OVERLAY, Texture.class));
    }

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
        return new CharacterController(map, character, actorMap, blueOverlay);
    }

    public TerrainController terrainController(Map map, Terrain terrain) {
        return new TerrainController(map, terrain, actorMap);
    }
}
