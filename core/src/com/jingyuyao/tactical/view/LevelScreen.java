package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.model.Level;

public class LevelScreen extends ScreenAdapter {
    private final Level level;
    private final MapView mapView;
    private final MapUI mapUI;

    LevelScreen(
            Level level,
            MapView mapView,
            MapUI mapUI
    ) {
        this.level = level;
        this.mapView = mapView;
        this.mapUI = mapUI;
    }

    @Override
    public void render(float delta) {
        mapUI.act(delta);
        mapView.act(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    public MapView getMapView() {
        return mapView;
    }

    public MapUI getMapUI() {
        return mapUI;
    }
}
