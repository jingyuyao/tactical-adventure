package com.jingyuyao.tactical.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;

public class MapFactory {
    private static final String TERRAIN_LAYER = "terrain";

    public static Map create(TiledMap tiledMap) {
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");

        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "MapView height must be > 0");
        Preconditions.checkArgument(width>0, "MapView width must be > 0");

        Map map = new Map(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                Terrain terrain = TerrainFactory.create(x, y, cell);
                map.set(terrain, x, y);
            }
        }

        // Testing
        map.addCharacter(new Character("john", 5, 5, 10));
        map.addCharacter(new Character("billy", 10, 10, 5));

        return map;
    }
}
