package com.jingyuyao.tactical.map;

/**
 * Created by jingyu on 12/10/16.
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
