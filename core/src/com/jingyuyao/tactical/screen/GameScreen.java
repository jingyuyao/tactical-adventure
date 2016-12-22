package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.controller.MapController;
import com.jingyuyao.tactical.view.MapUI;
import com.jingyuyao.tactical.view.MapView;

/**
 * Glues together MVC components to create a screen.
 */
public class GameScreen extends ScreenAdapter {
    private final TacticalAdventure game;
    private final MapView mapView;
    private final MapUI mapUI;
    private final MapController mapController;

    // TODO: Probably need to create a special "GameController" class when input handling gets complicated
    GameScreen(TacticalAdventure game, MapView mapView, MapUI mapUI, MapController mapController) {
        this.game = game;
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
