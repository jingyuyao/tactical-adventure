package com.jingyuyao.tactical.model.mark.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.Marker;
import java.util.Map;

public class HideMarking extends AbstractEvent<Map<MapObject, Marker>> {

  public HideMarking(Map<MapObject, Marker> object) {
    super(object);
  }
}
