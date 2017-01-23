package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * Super class of all the objects on the game grid.
 */
public abstract class MapObject {

  private Coordinate coordinate;

  public MapObject(Coordinate coordinate) {
    this.coordinate = coordinate;
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
