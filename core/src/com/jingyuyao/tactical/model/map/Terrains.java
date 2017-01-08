package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.BindingAnnotation;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.common.ManagedBy;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains a mapping of {@link Coordinate} to {@link Terrain}.
 */
// TODO: sync changes terrains' coordinates with grid when terrain can change locations
@Singleton
public class Terrains extends EventBusObject
    implements ManagedBy<NewMap, ClearMap>, Iterable<Terrain> {

  // We rely on coordinates' hashing invariant
  private final Map<Coordinate, Terrain> terrainMap;
  private int width;
  private int height;

  @Inject
  Terrains(EventBus eventBus, @BackingTerrainMap Map<Coordinate, Terrain> terrainMap) {
    super(eventBus);
    this.terrainMap = terrainMap;
    register();
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    for (Terrain terrain : data.getTerrains()) {
      terrainMap.put(terrain.getCoordinate(), terrain);
    }
    width = data.getWidth();
    height = data.getHeight();
    validateRectangular();
  }

  @Subscribe
  @Override
  public void dispose(ClearMap clearMap) {
    terrainMap.clear();
    width = 0;
    height = 0;
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

  @Override
  public Iterator<Terrain> iterator() {
    return terrainMap.values().iterator();
  }

  private void validateRectangular() throws IllegalStateException {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (!terrainMap.containsKey(new Coordinate(x, y))) {
          throw new IllegalStateException("Grid is not fully populated");
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
