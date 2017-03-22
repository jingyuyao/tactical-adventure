package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;

public class SelectCell {

  private final Coordinate coordinate;
  private final Cell cell;

  public SelectCell(Coordinate coordinate, Cell cell) {
    this.coordinate = coordinate;
    this.cell = cell;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Cell getCell() {
    return cell;
  }
}
