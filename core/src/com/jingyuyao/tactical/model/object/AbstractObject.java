package com.jingyuyao.tactical.model.object;

import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Markers;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractObject extends Observable {
    /**
     * List of marker drawn over this object.
     */
    private final List<Markers> markers;
    private Coordinate coordinate;

    AbstractObject(int x, int y) {
        coordinate = new Coordinate(x, y);
        markers = new ArrayList<Markers>();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void addMarker(Markers marker) {
        markers.add(marker);
        setChanged();
        notifyObservers(new AddMarker(marker));
    }

    public void removeMarker(Markers marker) {
        markers.remove(marker);
        setChanged();
        notifyObservers(new RemoveMarker(marker));
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
                "markers=" + markers +
                ", coordinate=" + coordinate +
                "}";
    }

    public static class AddMarker {
        private final Markers marker;

        private AddMarker(Markers marker) {
            this.marker = marker;
        }

        public Markers getMarker() {
            return marker;
        }
    }

    public static class RemoveMarker {
        private final Markers marker;

        private RemoveMarker(Markers marker) {
            this.marker = marker;
        }

        public Markers getMarker() {
            return marker;
        }
    }
}
