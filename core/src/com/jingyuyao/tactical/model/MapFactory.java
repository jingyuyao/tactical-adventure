package com.jingyuyao.tactical.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

public class MapFactory {
    private static final String TERRAIN_LAYER = "terrain";

    private final TerrainFactory terrainFactory;

    @Inject
    public MapFactory(TerrainFactory terrainFactory) {
        this.terrainFactory = terrainFactory;
    }

    public Map create(TiledMap tiledMap) {
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
                Terrain terrain = terrainFactory.create(x, y, cell);
                map.setTerrain(terrain, x, y);
            }
        }

        return map;
    }
}
