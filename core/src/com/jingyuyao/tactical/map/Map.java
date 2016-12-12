package com.jingyuyao.tactical.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;

/**
 * TODO: Make a factory that builds a map from a TiledMap
 */
public class Map {
    private static final String TERRAIN_LAYER = "terrain";

    private final TiledMap tiledMap;
    private final Terrain[][] terrain;
    private final int height;
    private final int width;

    public Map(final TiledMap tiledMap)    {
        this.tiledMap = tiledMap;

        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "Map must contain a terrain layer.");
        height = terrainLayer.getHeight();
        width = terrainLayer.getWidth();
        terrain = new Terrain[height][width];
    }
}
