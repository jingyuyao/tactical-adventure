package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

import java.util.Observable;

/**
 * An object on the game grid.
 */
public abstract class MapObject extends Observable {
    private Coordinate coordinate;

    MapObject(int x, int y) {
        coordinate = new Coordinate(x, y);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Sets the coordinate of this object to the new coordinate
     * Subclasses are responsible for notifying observers.
     * This method is purposely set to protected to enforce the above rule.
     */
    protected void setCoordinate(Coordinate newCoordinate) {
        coordinate = newCoordinate;
    }

    /**
     * Enables the visitor pattern for selection.
     *
     * I can't believe OOD actually taught me something useful.
     */
    public abstract void select(MapState mapState);

    @Override
    public String toString() {
        return "MapObject{" +
                "coordinate=" + coordinate +
                "}";
    }
}
