package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MapController {
    private final Stage stage;
    private final int worldWidth;
    private final int worldHeight;
    private final CameraController cameraController;
    private final InputMultiplexer inputMultiplexer;

    public MapController(Stage stage, int worldWidth, int worldHeight) {
        this.stage = stage;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        cameraController = new CameraController(stage.getViewport(), stage.getCamera(), worldWidth, worldHeight);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(cameraController);
        inputMultiplexer.addProcessor(stage);
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }
}
