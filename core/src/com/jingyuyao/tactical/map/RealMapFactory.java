package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

/**
 * Not the cleanest approach but at least {@link Map} can now receive injections via this class.
 */
public class RealMapFactory implements MapFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final float TILE_SCALE = 1f; // Relative to map size
    private static final int MAP_WIDTH = 30; // # tiles
    private static final int MAP_HEIGHT = 20;
    private static final String TERRAIN_LAYER = "terrain";

    private final TerrainFactory terrainFactory;

    @Inject
    public RealMapFactory(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
    }

    /**
     * Creates a {@link Map} where (0,0) is on the bottom left. Each tile has a size of one.
     * Viewport's size is relative to tile size.
     */
    @Override
    public Map create(TiledMap tiledMap) {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        Stage stage = new Stage(viewport);
        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE);

        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "Map must contain a terrain layer.");
        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "Map height must be > 0");
        Preconditions.checkArgument(width>0, "Map width must be > 0");

        Terrain[][] terrainMap = new Terrain[height][width];
        Map map = new Map(tiledMap, stage, mapRenderer, terrainMap);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                Terrain terrain = terrainFactory.create(map, cell, x, y, TILE_SCALE, TILE_SCALE);
                terrainMap[y][x] = terrain;
                stage.addActor(terrain);
            }
        }

        return map;
    }
}
