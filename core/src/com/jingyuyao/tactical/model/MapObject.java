package com.jingyuyao.tactical.model;

import java.util.Observable;

/**
 * An object on the game grid.
 */
public class MapObject extends Observable implements HasCoordinate {
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
     * Call <b>{@link #notifyObservers()}</b> yourself!
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
