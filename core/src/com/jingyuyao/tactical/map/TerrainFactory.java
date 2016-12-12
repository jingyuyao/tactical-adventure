package com.jingyuyao.tactical.map;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public interface TerrainFactory {
    Terrain create(Map map, TiledMapTileLayer.Cell cell, float x, float y, float width, float height);
}
