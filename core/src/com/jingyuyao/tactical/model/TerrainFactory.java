package com.jingyuyao.tactical.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

class TerrainFactory {
    private static final String TYPE_KEY = "type";

    // TODO: Move this to a "loader" in its own package
    static Terrain create(int x, int y, TiledMapTileLayer.Cell cell) {
        MapProperties tileProperties = cell.getTile().getProperties();
        Terrain.Type type = Terrain.Type.NORMAL;
        if (tileProperties.containsKey(TYPE_KEY)) {
            String tileType = tileProperties.get(TYPE_KEY, String.class);
            try {
                type = Terrain.Type.valueOf(tileType);
            } catch (IllegalArgumentException e) {
                Gdx.app.log("Terrain", String.format("invalid type %s", tileType));
            }
        }
        return new Terrain(x, y, type);
    }
}
