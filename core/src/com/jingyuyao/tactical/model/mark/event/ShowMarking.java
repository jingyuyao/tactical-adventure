package com.jingyuyao.tactical.model.mark.event;

import com.google.common.collect.ImmutableMultimap;
import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;

public class ShowMarking extends AbstractEvent<ImmutableMultimap<MapObject, Marker>> {

  public ShowMarking(ImmutableMultimap<MapObject, Marker> object) {
    super(object);
  }
}
