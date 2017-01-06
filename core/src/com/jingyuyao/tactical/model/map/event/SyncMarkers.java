package com.jingyuyao.tactical.model.map.event;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;

public class SyncMarkers extends AbstractEvent<MapObject> {

  private final ImmutableList<Marker> markers;

  public SyncMarkers(MapObject object, Iterable<Marker> markers) {
    super(object);
    this.markers = ImmutableList.copyOf(markers);
  }

  public ImmutableList<Marker> getMarkers() {
    return markers;
  }
}
