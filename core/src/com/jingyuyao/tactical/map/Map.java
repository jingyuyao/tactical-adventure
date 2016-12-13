package com.jingyuyao.tactical.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
        multiplexer.addProcessor(this.new MapMovementProcessor());
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

    private class MapMovementProcessor extends InputAdapter {
        private static final float HORIZONTAL_EDGE = 0.15f;
        private static final float VERTICAL_EDGE = 0.1f;
        private static final float MOVEMENT_DELTA = 0.2f;
        private static final long STARTING_DELAY = 300L; // ms
        private static final long REPEAT_DELAY = 10L; // ms

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        private ScheduledFuture<?> screenMoverHandle;
        private int currentX;
        private int currentY;

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            screenMoverHandle =
                    scheduler.scheduleAtFixedRate(screenMover, STARTING_DELAY, REPEAT_DELAY, TimeUnit.MILLISECONDS);
            currentX = screenX;
            currentY = screenY;
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (screenMoverHandle != null) screenMoverHandle.cancel(false);
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            currentX = screenX;
            currentY = screenY;
            return false;
        }

        private final Runnable screenMover = new Runnable() {
            @Override
            public void run() {
                int screenWidth = stage.getViewport().getScreenWidth();
                int screenHeight = stage.getViewport().getScreenHeight();
                float xEdgeWidth = HORIZONTAL_EDGE * (float)screenWidth;
                float yEdgeHeight = VERTICAL_EDGE * (float)screenHeight;
                float xDelta = 0f;
                float yDelta = 0f;

                if (currentX < xEdgeWidth) {
                    xDelta = -MOVEMENT_DELTA;
                } else if (currentX > screenWidth - xEdgeWidth) {
                    xDelta = MOVEMENT_DELTA;
                }

                if (currentY < yEdgeHeight) {
                    yDelta = MOVEMENT_DELTA;
                } else if (currentY > screenHeight - yEdgeHeight) {
                    yDelta = -MOVEMENT_DELTA;
                }

                Camera camera = stage.getCamera();
                camera.translate(xDelta, yDelta,0);
                camera.update();
            }
        };
    }
}
