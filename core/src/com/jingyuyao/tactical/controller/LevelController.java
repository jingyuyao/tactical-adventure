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
    private final MapView mapView;
    private final MapUI mapUI;
    private final TerrainGrid terrainGrid;

    @Inject
    LevelController(MapView mapView, MapUI mapUI, TerrainGrid terrainGrid) {
        this.mapView = mapView;
        this.mapUI = mapUI;
        this.terrainGrid = terrainGrid;
    }

    public void initiateControl() {
        Stage world = mapView.getStage();
        Stage ui = mapUI.getStage();
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
