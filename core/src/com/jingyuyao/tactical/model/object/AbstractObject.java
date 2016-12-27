package com.jingyuyao.tactical.model.object;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Markers;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractObject extends Observable {
    private final Set<Markers> markers;
    private Coordinate coordinate;

    AbstractObject(int x, int y) {
        coordinate = new Coordinate(x, y);
        markers = new HashSet<Markers>();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void addMarker(Markers marker) {
        markers.add(marker);
        setChanged();
        notifyObservers(new MarkerChange(markers));
    }

    public void clearMarkers() {
        markers.clear();
        setChanged();
        notifyObservers(new MarkerChange(markers));
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

    public static class MarkerChange {
        private final ImmutableSet<Markers> markers;

        protected MarkerChange(Set<Markers> markers) {
            this.markers = ImmutableSet.copyOf(markers);
        }

        public ImmutableSet<Markers> getMarkers() {
            return markers;
        }
    }
}
