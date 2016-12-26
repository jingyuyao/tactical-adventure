package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.view.LevelScreen;

public class LevelController {
    public static void initiateControl(LevelScreen levelScreen, Level level) {
        Stage world = levelScreen.getMapView().getWorld();
        Stage ui = levelScreen.getMapUI().getUi();
        int worldWidth = level.getMap().getWidth();
        int worldHeight = level.getMap().getHeight();

        DragCameraController dragCameraController =
                new DragCameraController(worldWidth, worldHeight, world.getViewport());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(ui);
        inputMultiplexer.addProcessor(dragCameraController);
        inputMultiplexer.addProcessor(world);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
