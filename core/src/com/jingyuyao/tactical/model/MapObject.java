package com.jingyuyao.tactical.model;

/**
 * An object on the game grid.
 */
public class MapObject {
    private int x;
    private int y;

    MapObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
