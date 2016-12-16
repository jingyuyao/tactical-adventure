package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.view.MapView;

public class GameScreen extends ScreenAdapter {
    private final TacticalAdventure game;
    private final MapView mapView;

    GameScreen(TacticalAdventure game, MapView mapView) {
        this.game = game;
        this.mapView = mapView;

        Gdx.input.setInputProcessor(mapView.getInputProcessor());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mapView.act(delta);
		mapView.draw();
    }

    @Override
    public void resize(int width, int height) {
        // This is very important...
        mapView.resize(width, height);
    }
}
