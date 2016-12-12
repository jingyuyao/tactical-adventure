package com.jingyuyao.tactical.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;

/**
 * Game representation of {@link TiledMap}
 */
public class Map {
    private static final String TERRAIN_LAYER = "terrain";

    private final TiledMap tiledMap;
    /**
     * (0,0) starts at bottom left just like {@link TiledMapTileLayer}.
     */
    private final Terrain[][] terrainMap;
    private final int height;
    private final int width;

    Map(final TiledMap tiledMap, final Terrain[][] terrainMap)    {
        this.tiledMap = tiledMap;
        this.terrainMap = terrainMap;
        height = terrainMap.length;
        width = terrainMap[0].length;
    }

    public static class MapFactory {
        public static Map create(final TiledMap tiledMap) {
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
                }
            }

            return new Map(tiledMap, terrainMap);
        }
    }
}
