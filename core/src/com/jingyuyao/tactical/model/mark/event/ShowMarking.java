package com.jingyuyao.tactical.model.mark.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;
import java.util.Map;

public class ShowMarking extends AbstractEvent<Map<MapObject, Marker>> {

  public ShowMarking(Map<MapObject, Marker> object) {
    super(object);
  }
}
