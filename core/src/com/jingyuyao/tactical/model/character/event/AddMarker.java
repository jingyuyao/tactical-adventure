package com.jingyuyao.tactical.model.character.event;

import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;

public class AddMarker extends ObjectEvent<MapObject> {
  private final Marker marker;

  public AddMarker(MapObject object, Marker marker) {
    super(object);
    this.marker = marker;
  }

  public Marker getMarker() {
    return marker;
  }
}
