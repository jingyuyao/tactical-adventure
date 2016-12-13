package com.jingyuyao.tactical.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Game representation of {@link TiledMap}.
 * Map has its own stage with actors receiving touch input.
 * It is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class Map {
    private final Stage stage;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    /**
     * (0,0) starts at bottom left just like {@link TiledMapTileLayer}.
     */
    private final Terrain[][] terrainMap;
    private final int height;
    private final int width;

    Map(TiledMap tiledMap, Stage stage, OrthogonalTiledMapRenderer mapRenderer, Terrain[][] terrainMap) {
        this.stage = stage;
        this.tiledMap = tiledMap;
        this.terrainMap = terrainMap;
        this.mapRenderer = mapRenderer;
        height = terrainMap.length;
        width = terrainMap[0].length;

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new MapMovementProcessor(stage));
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        mapRenderer.setView((OrthographicCamera) stage.getCamera());
        mapRenderer.render();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
