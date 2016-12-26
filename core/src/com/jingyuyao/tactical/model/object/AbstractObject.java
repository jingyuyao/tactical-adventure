package com.jingyuyao.tactical.model.object;

import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.Observable;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractObject extends Observable {
    private Coordinate coordinate;

    AbstractObject(int x, int y) {
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

    /**
     * Enables the visitor pattern for highlight.
     */
    public abstract void highlight(Highlighter highlighter);

    @Override
    public String toString() {
        return "AbstractObject{" +
                "coordinate=" + coordinate +
                "}";
    }
}
