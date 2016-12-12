package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Renders our tile map.
 * TODO: Add camera movement controls
 * TODO: Create factory to extract start position
 */
public class MapRenderer {
    private final float UNIT_SCALE = 1/32f;
    private final int WIDTH = 25;
    private final int HEIGHT = 20;

    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;

    public MapRenderer(final TiledMap tiledMap) {
        renderer = new OrthogonalTiledMapRenderer(tiledMap, UNIT_SCALE);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
    }

    public void render() {
        // Camera needs to be updated whenever its properties change
        camera.update();
        // The view needs to be set for every frame
        renderer.setView(camera);
        renderer.render();
    }
}
