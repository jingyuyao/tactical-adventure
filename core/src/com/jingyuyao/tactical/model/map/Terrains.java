package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.AddTerrain;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains a mapping of {@link Coordinate} to {@link Terrain}.
 */
// TODO: sync changes terrains' coordinates with grid when terrain can change locations
@Singleton
public class Terrains implements Iterable<Terrain> {

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

  public Terrain get(Coordinate coordinate) {
    return terrainMap.get(coordinate);
  }

  public Iterable<Terrain> getAll(Iterable<Coordinate> coordinates) {
    return Iterables.transform(coordinates, new Function<Coordinate, Terrain>() {
      @Override
      public Terrain apply(Coordinate input) {
        return get(input);
      }
    });
  }

  public boolean contains(Coordinate coordinate) {
    return terrainMap.containsKey(coordinate);
  }

  /**
   * Returns the neighbors of {@code from}.
   *
   * @return Randomized list of neighbors
   */
  public ImmutableList<Terrain> getNeighbors(final Coordinate from) {
    Iterable<Terrain> neighborIterable =
        FluentIterable
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
                return get(input);
              }
            });

    List<Terrain> neighbors = Lists.newArrayList(neighborIterable);
    Collections.shuffle(neighbors);
    return ImmutableList.copyOf(neighbors);
  }

  @Override
  public Iterator<Terrain> iterator() {
    return terrainMap.values().iterator();
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

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingTerrainMap {

  }
}
