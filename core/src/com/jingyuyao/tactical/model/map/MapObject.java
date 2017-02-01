package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.state.MapState;

public interface MapObject {

  /**
   * Enables the visitor pattern for selection.
   */
  void select(MapState mapState);

  Coordinate getCoordinate();

  void setCoordinate(Coordinate coordinate);
}
