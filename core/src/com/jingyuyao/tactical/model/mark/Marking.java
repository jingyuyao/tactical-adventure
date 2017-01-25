package com.jingyuyao.tactical.model.mark;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.MapObject;
import java.util.Collection;
import java.util.Map.Entry;
import javax.inject.Inject;

/**
 * A marking is a map of {@link MapObject} to {@link Marker}.
 */
public class Marking {

  private final ImmutableMultimap<MapObject, Marker> markers;
  private boolean applied = false;

  /**
   * Creates a marking with the given {@code markers} map.
   */
  @Inject
  Marking(@Assisted ImmutableMultimap<MapObject, Marker> markers) {
    this.markers = markers;
  }

  /**
   * Applies this marking if it has not been applied.
   */
  public void apply() {
    if (!applied) {
      applied = true;
      for (Entry<MapObject, Collection<Marker>> entry : markers.asMap().entrySet()) {
        for (Marker marker : entry.getValue()) {
          entry.getKey().addMarker(marker);
        }
      }
    }
  }

  /**
   * Clears the {@link #markers} held by this marking if it has been applied.
   */
  public void clear() {
    if (applied) {
      applied = false;
      for (Entry<MapObject, Collection<Marker>> entry : markers.asMap().entrySet()) {
        for (Marker marker : entry.getValue()) {
          entry.getKey().removeMarker(marker);
        }
      }
    }
  }
}
