package com.jingyuyao.tactical.model.map;

import com.google.common.collect.Multiset;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractMapObject<T extends MapObjectData> implements MapObject {

  private final T data;
  private final Multiset<Marker> markers;

  protected AbstractMapObject(T data, Multiset<Marker> markers) {
    this.data = data;
    this.markers = markers;
  }

  @Override
  public Coordinate getCoordinate() {
    return data.getCoordinate();
  }

  @Override
  public void setCoordinate(Coordinate newCoordinate) {
    data.setCoordinate(newCoordinate);
  }

  @Override
  public Multiset<Marker> getMarkers() {
    return markers;
  }

  @Override
  public void addMarker(Marker marker) {
    markers.add(marker);
  }

  @Override
  public void removeMarker(Marker marker) {
    markers.remove(marker);
  }

  protected T getData() {
    return data;
  }
}
