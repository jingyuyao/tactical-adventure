package com.jingyuyao.tactical.model.mark;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.map.MapObject;
import java.util.Map;
import javax.inject.Inject;

/**
 * A marking is a map of {@link MapObject} to {@link Marker}. It is tied to a {@link Character}
 * {@link #owner} and cleared upon death.
 */
public class Marking extends EventBusObject {

  private final Character owner;
  private final Map<MapObject, Marker> markers;
  private boolean applied = false;
  private boolean cleared = false;

  /**
   * Creates a marking with the given {@code markers} map.
   */
  @Inject
  Marking(EventBus eventBus, @Assisted Character owner, @Assisted Map<MapObject, Marker> markers) {
    super(eventBus);
    this.owner = owner;
    this.markers = markers;
    register();
  }

  @Subscribe
  public void removeCharacter(RemoveCharacter removeCharacter) {
    if (removeCharacter.matches(owner)) {
      clear();
    }
  }

  /**
   * Applies this marking if it has not been applied and {@link #clear()} has not been called. Also
   * observes {@link #owner}'s death to clear itself.
   */
  public void apply() {
    if (cleared || applied) {
      return;
    }

    for (Map.Entry<MapObject, Marker> entry : markers.entrySet()) {
      entry.getKey().addMarker(entry.getValue());
    }
    applied = true;
  }

  /**
   * Clears the {@link #markers} held by this marking and prevent further {@link #apply()}.
   */
  public void clear() {
    if (cleared) {
      return;
    }
    // Set flag immediately so apply() would not get called for the rare cases where
    // clear() is called before apply()
    cleared = true;
    unregister();

    if (!applied) {
      return;
    }

    for (Map.Entry<MapObject, Marker> entry : markers.entrySet()) {
      entry.getKey().removeMarker(entry.getValue());
    }
  }
}
