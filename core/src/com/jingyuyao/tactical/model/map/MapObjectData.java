package com.jingyuyao.tactical.model.map;

public class MapObjectData {

  private Coordinate coordinate;

  protected MapObjectData() {
  }

  public MapObjectData(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }
}
