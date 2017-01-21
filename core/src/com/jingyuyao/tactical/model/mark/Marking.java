package com.jingyuyao.tactical.model.mark;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.event.HideMarking;
import com.jingyuyao.tactical.model.mark.event.ShowMarking;
import java.util.Map;
import javax.inject.Inject;

/**
 * A marking is a map of {@link MapObject} to {@link Marker}.
 */
public class Marking extends EventBusObject {

  private final Map<MapObject, Marker> markers;
  private boolean applied = false;

  /**
   * Creates a marking with the given {@code markers} map.
   */
  @Inject
  Marking(EventBus eventBus, @Assisted Map<MapObject, Marker> markers) {
    super(eventBus);
    this.markers = markers;
  }

  /**
   * Applies this marking if it has not been applied.
   */
  public void apply() {
    if (!applied) {
      applied = true;
      post(new ShowMarking(markers));
    }
  }

  /**
   * Clears the {@link #markers} held by this marking if it has been applied.
   */
  public void clear() {
    if (applied) {
      applied = false;
      post(new HideMarking(markers));
    }
  }
}
