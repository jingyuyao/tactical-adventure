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
  private int width;
  private int height;

  @Inject
  Terrains(
      @ModelEventBus EventBus eventBus,
      @BackingTerrainMap Map<Coordinate, Terrain> terrainMap) {
    this.eventBus = eventBus;
    this.terrainMap = terrainMap;
  }

  public void initialize(Iterable<Terrain> terrains, int width, int height) {
    for (Terrain terrain : terrains) {
      terrainMap.put(terrain.getCoordinate(), terrain);
      eventBus.post(new AddTerrain(terrain));
    }
    this.width = width;
    this.height = height;
    validateRectangular();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean contains(Coordinate coordinate) {
    return terrainMap.containsKey(coordinate);
  }

  public Iterable<Terrain> getAll(Iterable<Coordinate> coordinates) {
    return Iterables.transform(coordinates, new Function<Coordinate, Terrain>() {
      @Override
      public Terrain apply(Coordinate input) {
        return terrainMap.get(input);
      }
    });
  }

  public Iterable<Terrain> getNeighbors(final Coordinate from) {
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

  private void validateRectangular() throws IllegalStateException {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (!terrainMap.containsKey(new Coordinate(x, y))) {
          throw new IllegalStateException("Terrains is not fully populated");
        }
      }
    }
  }
}
