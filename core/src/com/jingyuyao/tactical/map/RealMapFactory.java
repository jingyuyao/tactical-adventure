package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.common.base.Preconditions;

/**
 * Not the cleanest approach but at least {@link Map} can now receive injections via this class.
 */
public class RealMapFactory implements MapFactory {
    private static final float TILE_SCALE = 1/32f;
    private static final int MAP_WIDTH = 25; // # tiles
    private static final int MAP_HEIGHT = 20;
    private static final String TERRAIN_LAYER = "terrain";

    @Override
    public Map create(TiledMap tiledMap) {
        Stage stage = createStage();
        OrthogonalTiledMapRenderer mapRenderer = createRenderer(tiledMap);
        Terrain[][] terrainMap = createTerrain(tiledMap, stage);

        return new Map(tiledMap, stage, mapRenderer, terrainMap);
    }

    private Stage createStage() {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        return new Stage(viewport);
    }

    private OrthogonalTiledMapRenderer createRenderer(TiledMap tiledMap) {
        return new OrthogonalTiledMapRenderer(tiledMap, TILE_SCALE);
    }

    private Terrain[][] createTerrain(TiledMap tiledMap, Stage stage) {
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
        return terrainMap;
    }
}
