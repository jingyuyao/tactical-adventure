package com.jingyuyao.tactical.map;

/**
 * TODO: Make a factory that builds a block from a TiledMapTile
 */
public class Block {
    private Type type;

    public Block() {
        this(Type.NORMAL);
    }

    public Block(Type type) {
        this.type = type;
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
