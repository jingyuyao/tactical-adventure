package com.jingyuyao.tactical.model.mark;

import com.google.common.collect.ImmutableMultimap;
import com.jingyuyao.tactical.model.map.MapObject;

public interface MarkingFactory {

  Marking create(ImmutableMultimap<MapObject, Marker> markers);
}
