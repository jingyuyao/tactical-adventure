package com.jingyuyao.tactical.model.mark;

import com.jingyuyao.tactical.model.map.MapObject;
import java.util.Map;

/**
 * A marking is a map of {@link MapObject} to {@link Marker}.
 */
public class Marking {

  private final Map<MapObject, Marker> markers;
  private boolean applied = false;

  /**
   * Creates a marking with the given {@code markers} map.
   */
  public Marking(Map<MapObject, Marker> markers) {
    this.markers = markers;
  }

  /**
   * Applies this marking if it has not been applied.
   */
  public void apply() {
    if (applied) {
      return;
    }

    for (Map.Entry<MapObject, Marker> entry : markers.entrySet()) {
      entry.getKey().addMarker(entry.getValue());
    }
    applied = true;
  }

  /**
   * Clears the {@link #markers} held by this marking if it has been applied.
   */
  public void clear() {
    if (!applied) {
      return;
    }

    for (Map.Entry<MapObject, Marker> entry : markers.entrySet()) {
      entry.getKey().removeMarker(entry.getValue());
    }
    applied = false;
  }
}
