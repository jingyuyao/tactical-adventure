package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

import java.util.Observable;

/**
 * An object on the game grid.
 */
public abstract class MapObject extends Observable implements HasCoordinate {
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
     * Enables the visitor pattern for selection.
     *
     * I can't believe OOD actually taught me something useful.
     */
    public abstract void select(MapState mapState);

    /**
     * Call <b>{@link #setChanged()} and {@link #notifyObservers()}</b> yourself!
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
