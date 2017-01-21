package com.jingyuyao.tactical.model.mark;

import com.jingyuyao.tactical.model.map.MapObject;
import java.util.Map;

public interface MarkingFactory {

  Marking create(Map<MapObject, Marker> markers);
}
