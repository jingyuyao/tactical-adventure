package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.map.Map;

public class GameScreen extends ScreenAdapter {
    private final TacticalAdventure game;
    private final Map map;

    public GameScreen(TacticalAdventure game, Map map) {
        this.game = game;
        this.map = map;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		map.act(delta);
		map.draw();
    }
}
