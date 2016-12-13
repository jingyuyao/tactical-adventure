package com.jingyuyao.tactical.map;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
    private final InputMultiplexer inputMultiplexer;

    /**
     * (0,0) starts at bottom left just like {@link TiledMapTileLayer}.
     */
    private final Terrain[][] terrainMap;
    private final int worldWidth;
    private final int worldHeight;

    Map(TiledMap tiledMap, Stage stage, OrthogonalTiledMapRenderer mapRenderer, Terrain[][] terrainMap) {
        this.stage = stage;
        this.tiledMap = tiledMap;
        this.terrainMap = terrainMap;
        this.mapRenderer = mapRenderer;
        worldHeight = terrainMap.length;
        worldWidth = terrainMap[0].length;

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new MapMovementProcessor(stage, worldWidth, worldHeight));
        inputMultiplexer.addProcessor(stage);
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

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }
}
