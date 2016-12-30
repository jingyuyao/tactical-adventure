package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.TerrainGrid;
import com.jingyuyao.tactical.view.MapUI;
import com.jingyuyao.tactical.view.MapView;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LevelController {
    private final MapUI mapUI;
    private final TerrainGrid terrainGrid;

    @Inject
    LevelController(MapUI mapUI, TerrainGrid terrainGrid) {
        this.mapUI = mapUI;
        this.terrainGrid = terrainGrid;
    }

    public void initiateControl(MapView mapView) {
        Stage world = mapView.getWorld();
        Stage ui = mapUI.getUi();
        int worldWidth = terrainGrid.getWidth();
        int worldHeight = terrainGrid.getHeight();

        DragCameraController dragCameraController =
                new DragCameraController(worldWidth, worldHeight, world.getViewport());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(ui);
        inputMultiplexer.addProcessor(dragCameraController);
        inputMultiplexer.addProcessor(world);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
