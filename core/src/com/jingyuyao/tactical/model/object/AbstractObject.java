package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.event.AddMarker;
import com.jingyuyao.tactical.model.event.RemoveMarker;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.util.DisposableObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractObject extends DisposableObject {
    /**
     * List of marker drawn over this object.
     */
    private final List<Marker> markers;
    private Coordinate coordinate;

    // TODO: inject initial coordinate and markers?
    AbstractObject(EventBus eventBus, int x, int y) {
        super(eventBus);
        coordinate = new Coordinate(x, y);
        markers = new ArrayList<Marker>();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void addMarker(Marker marker) {
        markers.add(marker);
        getEventBus().post(new AddMarker(this, marker));
    }

    public void removeMarker(Marker marker) {
        markers.remove(marker);
        getEventBus().post(new RemoveMarker(this, marker));
    }

    /**
     * Sets the coordinate of this object to the new coordinate
     * Subclasses are responsible for posting changes.
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

}
