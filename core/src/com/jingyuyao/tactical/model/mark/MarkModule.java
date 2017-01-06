package com.jingyuyao.tactical.model.mark;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.mark.MarkingFactory.InitialMarkerMap;
import java.util.HashMap;
import java.util.Map;

public class MarkModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MarkingFactory.class);
  }

  @Provides
  @InitialMarkerMap
  Map<MapObject, Marker> provideInitialMarkerMap() {
    return new HashMap<MapObject, Marker>();
  }
}
