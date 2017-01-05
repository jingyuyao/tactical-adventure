package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.mark.Marker;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class MapFactory {

  private final EventBus eventBus;
  private final Provider<List<Marker>> markersProvider;

  @Inject
  public MapFactory(
      EventBus eventBus, @MapObject.InitialMarkers Provider<List<Marker>> markersProvider) {
    this.eventBus = eventBus;
    this.markersProvider = markersProvider;
  }

  public Terrain createTerrain(int x, int y, Terrain.Type type) {
    return new Terrain(eventBus, new Coordinate(x, y), markersProvider.get(), type);
  }
}
