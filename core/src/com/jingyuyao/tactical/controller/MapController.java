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
public class MapController {
    private final Stage mapViewStage;
    private final Stage mapUIStage;
    private final TerrainGrid terrainGrid;

    @Inject
    MapController(
            @MapView.MapViewStage Stage mapViewStage,
            @MapUI.MapUiStage Stage mapUIStage,
            TerrainGrid terrainGrid) {
        this.mapViewStage = mapViewStage;
        this.mapUIStage = mapUIStage;
        this.terrainGrid = terrainGrid;
    }

    public void initiateControl() {
        int worldWidth = terrainGrid.getWidth();
        int worldHeight = terrainGrid.getHeight();

        DragCameraController dragCameraController =
                new DragCameraController(worldWidth, worldHeight, mapViewStage.getViewport());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mapUIStage);
        inputMultiplexer.addProcessor(dragCameraController);
        inputMultiplexer.addProcessor(mapViewStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
