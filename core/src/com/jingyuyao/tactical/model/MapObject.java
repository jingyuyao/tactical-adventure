package com.jingyuyao.tactical.model;

/**
 * An object on the game grid.
 */
public class MapObject implements HasCoordinate {
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

    // Setters are protected so children can manage how they are updated
    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
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
