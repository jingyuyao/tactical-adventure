package com.jingyuyao.tactical.model;

import java.util.Collection;
import java.util.Observable;

/**
 * An object with a (x,y) coordinate that notify its observers when its position is changed.
 *
 * Always use {@link #containedIn(Collection)} when checking if this {@link Coordinate} belongs in a {@link Collection}.
 */
public class Coordinate extends Observable {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Updates the position of this {@link Coordinate} and notify its observers.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        setChanged();
        notifyObservers();
    }

    public boolean sameCoordinateAs(Coordinate other) {
        return getX() == other.getX() && getY() == other.getY();
    }

    public boolean containedIn(Collection<Coordinate> coordinates) {
        for (Coordinate c : coordinates) {
            if (sameCoordinateAs(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + getX() +
                ", y=" + getY() +
                "}";
    }
}
