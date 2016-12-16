package com.jingyuyao.tactical.model;

public class Terrain extends MapObject {
    private Type type;

    Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
