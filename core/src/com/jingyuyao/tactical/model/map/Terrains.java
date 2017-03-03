package com.jingyuyao.tactical.model.map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.event.AddTerrain;
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

  public void add(Terrain terrain) {
    terrainMap.put(terrain.getCoordinate(), terrain);
    eventBus.post(new AddTerrain(terrain));
    maxWidth = Math.max(maxWidth, terrain.getCoordinate().getX());
    maxHeight = Math.max(maxHeight, terrain.getCoordinate().getY());
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public boolean contains(Coordinate coordinate) {
    return terrainMap.containsKey(coordinate);
  }

  public Terrain get(Coordinate coordinate) {
    return terrainMap.get(coordinate);
  }

  public Iterable<Terrain> getAll(Iterable<Coordinate> coordinates) {
    return Iterables.transform(coordinates, new Function<Coordinate, Terrain>() {
      @Override
      public Terrain apply(Coordinate input) {
        return terrainMap.get(input);
      }
    });
  }

  Iterable<Terrain> getNeighbors(final Coordinate from) {
    return FluentIterable
        .from(Directions.ALL)
        .transform(new Function<Coordinate, Coordinate>() {
          @Override
          public Coordinate apply(Coordinate input) {
            return from.offsetBy(input);
          }
        })
        .filter(new Predicate<Coordinate>() {
          @Override
          public boolean apply(Coordinate input) {
            return contains(input);
          }
        })
        .transform(new Function<Coordinate, Terrain>() {
          @Override
          public Terrain apply(Coordinate input) {
            return terrainMap.get(input);
          }
        });
  }
}
