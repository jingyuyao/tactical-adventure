package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.TacticalAdventure;

public class GameScreen extends ScreenAdapter {
    private final float UNIT_SCALE = 1/32f;

    private final TacticalAdventure game;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;

    public GameScreen(TacticalAdventure game) {
        this.game = game;
        map = game.getAssetManager().get(Assets.TEST_MAP);
        renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 50, 50);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Camera needs to be updated whenever its properties change
		camera.update();
		// The view needs to be set for every frame
        renderer.setView(camera);
		renderer.render();
    }
}
