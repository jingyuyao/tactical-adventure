package com.jingyuyao.tactical.model.map;

import com.jingyuyao.tactical.model.state.SelectionHandler;

public interface MapObject {

  /**
   * Enables the visitor pattern for selection.
   */
  void select(SelectionHandler selectionHandler);

  Coordinate getCoordinate();

  void setCoordinate(Coordinate coordinate);
}
