package com.jingyuyao.tactical.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A terrain tile.
 */
public class Terrain {
    private static final String TYPE_KEY = "type";

    private final TiledMapTileLayer.Cell cell;
    private Type type;

    Terrain(final TiledMapTileLayer.Cell cell, Type type) {
        this.cell = cell;
        this.type = type;
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }

    public static class TerrainFactory {
        public static Terrain create(TiledMapTileLayer.Cell cell) {
            MapProperties tileProperties = cell.getTile().getProperties();
            Type type = Type.NORMAL;
            if (tileProperties.containsKey(TYPE_KEY)) {
                String tileType = tileProperties.get(TYPE_KEY, String.class);
                try {
                    type = Type.valueOf(tileType);
                } catch (IllegalArgumentException e) {
                    Gdx.app.log("Terrain", String.format("invalid type %s", tileType));
                }
            }
            return new Terrain(cell, type);
        }
    }
}
