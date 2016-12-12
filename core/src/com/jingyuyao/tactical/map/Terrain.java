package com.jingyuyao.tactical.map;

/**
 * TODO: Make a factory that builds a block from a TiledMapTile
 */
public class Terrain {
    private Type type;

    public Terrain() {
        this(Type.NORMAL);
    }

    public Terrain(Type type) {
        this.type = type;
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
