package com.jingyuyao.tactical.model.map;

import com.google.common.collect.ImmutableMultimap;
import java.util.Collection;
import java.util.Map.Entry;

/**
 * A marking is a map of {@link MapObject} to {@link Marker}.
 */
public class Marking {

  private final ImmutableMultimap<MapObject, Marker> markers;
  private boolean applied = false;

  /**
   * Creates a marking with the given {@code markers} map.
   */
  Marking(ImmutableMultimap<MapObject, Marker> markers) {
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

  public static class MarkingBuilder {

    private final ImmutableMultimap.Builder<MapObject, Marker> mapBuilder;

    public MarkingBuilder() {
      this.mapBuilder = ImmutableMultimap.builder();
    }

    public void put(MapObject object, Marker marker) {
      mapBuilder.put(object, marker);
    }

    public Marking build() {
      return new Marking(mapBuilder.build());
    }
  }
}
