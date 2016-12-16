package com.jingyuyao.tactical.model;

/**
 * An object on the game grid.
 */
public class MapObject {
    private int x;
    private int y;
    private boolean highlighted = false;

    MapObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public String toString() {
        return "MapObject{" +
                "x=" + x +
                ", y=" + y +
                ", highlighted=" + highlighted +
                '}';
    }
}
