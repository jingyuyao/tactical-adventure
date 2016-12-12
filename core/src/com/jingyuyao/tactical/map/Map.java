package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.common.base.Preconditions;

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

    Map(final Stage stage, final TiledMap tiledMap, final OrthogonalTiledMapRenderer mapRenderer,
            final Terrain[][] terrainMap) {
        this.stage = stage;
        this.tiledMap = tiledMap;
        this.terrainMap = terrainMap;
        this.mapRenderer = mapRenderer;
        height = terrainMap.length;
        width = terrainMap[0].length;
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        mapRenderer.setView((OrthographicCamera) stage.getCamera());
        mapRenderer.render();
    }

    public static class MapFactory {
        private static final float TILE_SCALE = 1/32f;
        private static final int MAP_WIDTH = 25; // # tiles
        private static final int MAP_HEIGHT = 20;
        private static final String TERRAIN_LAYER = "terrain";

        public static Map create(final TiledMap tiledMap) {
            OrthographicCamera camera = new OrthographicCamera();
            FitViewport viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
            Stage stage = new Stage(viewport);
            OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, TILE_SCALE);

            TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
            Preconditions.checkNotNull(terrainLayer, "Map must contain a terrain layer.");

            int height = terrainLayer.getHeight();
            int width = terrainLayer.getWidth();
            Preconditions.checkArgument(height>0, "Map height must be > 0");
            Preconditions.checkArgument(width>0, "Map width must be > 0");

            Terrain[][] terrainMap = new Terrain[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                    terrainMap[y][x] = Terrain.TerrainFactory.create(cell);
                    // TODO: Add terrain to stage
                }
            }

            return new Map(stage, tiledMap, mapRenderer, terrainMap);
        }
    }
}
