package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.map.MapModule.BackingTerrainMap;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains a mapping of {@link Coordinate} to {@link Terrain}.
 */
// TODO: sync changes terrains' coordinates with grid when terrain can change locations
@Singleton
public class Terrains {

  private final EventBus eventBus;
  // We rely on coordinates' hashing invariant
  private final Map<Coordinate, Terrain> terrainMap;
  private int maxWidth;
  private int maxHeight;

  @Inject
  Terrains(
      @ModelEventBus EventBus eventBus,
      @BackingTerrainMap Map<Coordinate, Terrain> terrainMap) {
    this.eventBus = eventBus;
    this.terrainMap = terrainMap;
  }
}
