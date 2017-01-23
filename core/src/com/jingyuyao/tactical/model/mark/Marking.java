package com.jingyuyao.tactical.model.mark;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.event.HideMarking;
import com.jingyuyao.tactical.model.mark.event.ShowMarking;
import javax.inject.Inject;

/**
 * A marking is a map of {@link MapObject} to {@link Marker}.
 */
public class Marking {

  private final EventBus eventBus;
  private final ImmutableMultimap<MapObject, Marker> markers;
  private boolean applied = false;

  /**
   * Creates a marking with the given {@code markers} map.
   */
  @Inject
  Marking(EventBus eventBus, @Assisted ImmutableMultimap<MapObject, Marker> markers) {
    this.eventBus = eventBus;
    this.markers = markers;
  }

  /**
   * Applies this marking if it has not been applied.
   */
  public void apply() {
    if (!applied) {
      applied = true;
      eventBus.post(new ShowMarking(markers));
    }
  }

  /**
   * Clears the {@link #markers} held by this marking if it has been applied.
   */
  public void clear() {
    if (applied) {
      applied = false;
      eventBus.post(new HideMarking(markers));
    }
  }
}
