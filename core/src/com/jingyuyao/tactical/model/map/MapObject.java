package com.jingyuyao.tactical.model.map;

import com.google.common.collect.Multiset;
import com.jingyuyao.tactical.model.state.MapState;

public interface MapObject {

  /**
   * Enables the visitor pattern for selection.
   */
  void select(MapState mapState);

  /**
   * Enables the visitor pattern for highlight.
   */
  void highlight(MapState mapState);

  Coordinate getCoordinate();

  void setCoordinate(Coordinate coordinate);

  Multiset<Marker> getMarkers();

  void addMarker(Marker marker);

  void removeMarker(Marker marker);
}
