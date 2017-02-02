package com.jingyuyao.tactical.model.map;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class AbstractMapObject implements MapObject {

  private Coordinate coordinate;

  protected AbstractMapObject() {
  }

  protected AbstractMapObject(Coordinate coordinate) {
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
