package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.view.MapUI;
import com.jingyuyao.tactical.view.MapView;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapController {
    private final Stage mapViewStage;
    private final Stage mapUIStage;
    private final Terrains terrains;

    @Inject
    MapController(
            @MapView.MapViewStage Stage mapViewStage,
            @MapUI.MapUiStage Stage mapUIStage,
            Terrains terrains) {
        this.mapViewStage = mapViewStage;
        this.mapUIStage = mapUIStage;
        this.terrains = terrains;
    }

    public void initiateControl() {
        int worldWidth = terrains.getWidth();
        int worldHeight = terrains.getHeight();

        DragCameraController dragCameraController =
                new DragCameraController(worldWidth, worldHeight, mapViewStage.getViewport());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mapUIStage);
        inputMultiplexer.addProcessor(dragCameraController);
        inputMultiplexer.addProcessor(mapViewStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
