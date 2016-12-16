package com.jingyuyao.tactical.model;

public class Terrain extends GridObject {
    private Type type;

    public Terrain(int x, int y, Type type) {
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
