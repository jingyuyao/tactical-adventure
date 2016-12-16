package com.jingyuyao.tactical.model;

/**
 * An object on the game grid.
 */
public class GridObject {
    private int x;
    private int y;

    GridObject(int x, int y) {
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
