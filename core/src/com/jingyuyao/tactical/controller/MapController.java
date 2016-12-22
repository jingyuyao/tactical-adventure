package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MapController {
    private final CameraController cameraController;
    private final InputMultiplexer inputMultiplexer;

    public MapController(Stage ui, Stage world, int worldWidth, int worldHeight) {
        cameraController = new CameraController(world.getViewport(), world.getCamera(), worldWidth, worldHeight);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(ui);
        inputMultiplexer.addProcessor(cameraController);
        inputMultiplexer.addProcessor(world);
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }
}
