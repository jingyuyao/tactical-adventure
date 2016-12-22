package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.model.Level;

public class LevelScreen extends ScreenAdapter {
    private final TacticalAdventure game;
    private final Level level;
    private final MapView mapView;
    private final MapUI mapUI;
    private final MapController mapController;

    LevelScreen(
            TacticalAdventure game,
            Level level,
            MapView mapView,
            MapUI mapUI,
            MapController mapController
    ) {
        this.game = game;
        this.level = level;
        this.mapView = mapView;
        this.mapUI = mapUI;
        this.mapController = mapController;

        Gdx.input.setInputProcessor(mapController.getInputProcessor());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapUI.act(delta);
        mapView.act(delta);
        mapView.draw();
        mapUI.draw();
    }

    @Override
    public void resize(int width, int height) {
        // This is very important...
        mapView.resize(width, height);
        mapUI.resize(width, height);
    }

    @Override
    public void dispose() {
        mapView.dispose();
        mapUI.dispose();
    }
}
