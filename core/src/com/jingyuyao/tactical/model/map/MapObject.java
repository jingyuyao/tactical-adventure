package com.jingyuyao.tactical.model.map;

import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class MapObject {

  private final Multiset<Marker> markers;
  private Coordinate coordinate;

  public MapObject(Coordinate coordinate, Multiset<Marker> markers) {
    this.coordinate = coordinate;
    this.markers = markers;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  /**
   * Sets the coordinate of this object to the new coordinate. Subclasses are responsible for
   * posting changes.
   */
  protected void setCoordinate(Coordinate newCoordinate) {
    coordinate = newCoordinate;
  }

  public Multiset<Marker> getMarkers() {
    return markers;
  }

  public void addMarker(Marker marker) {
    markers.add(marker);
  }

  public void removeMarker(Marker marker) {
    markers.remove(marker);
  }

  /**
   * Enables the visitor pattern for selection.
   *
   * <p>I can't believe OOD actually taught me something useful.
   */
  public abstract void select(MapState mapState);

  /**
   * Enables the visitor pattern for highlight.
   */
  public abstract void highlight(MapState mapState);
}
