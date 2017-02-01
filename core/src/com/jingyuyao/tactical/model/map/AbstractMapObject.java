package com.jingyuyao.tactical.model.map;

import com.google.common.collect.Multiset;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractMapObject implements MapObject {

  private transient final Multiset<Marker> markers;
  private Coordinate coordinate;

  protected AbstractMapObject(Multiset<Marker> markers) {
    this.markers = markers;
  }

  protected AbstractMapObject(Coordinate coordinate, Multiset<Marker> markers) {
    this(markers);
    this.coordinate = coordinate;
  }

  @Override
  public Coordinate getCoordinate() {
    return coordinate;
  }

  @Override
  public void setCoordinate(Coordinate newCoordinate) {
    coordinate = newCoordinate;
  }
}
