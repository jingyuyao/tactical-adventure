package com.jingyuyao.tactical.model;

/**
 * An object on the game grid.
 */
public class MapObject extends Updatable implements HasCoordinate {
    private int x;
    private int y;

    MapObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    /**
     * Call <b>{@link #update()}</b> yourself!
     */
    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "MapObject{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
