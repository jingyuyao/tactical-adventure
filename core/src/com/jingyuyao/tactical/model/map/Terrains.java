package com.jingyuyao.tactical.model.map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
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
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A terrain grid backed by a {@link Table}. Also contains convenience methods to work with our
 * {@link Coordinate} system.
 */
@Singleton
public class Terrains extends EventBusObject
    implements ManagedBy<NewMap, ClearMap>, Iterable<Terrain> {

  /**
   * (0,0) starts at bottom left.
   */
  private final Table<Integer, Integer, Terrain> table;
  private int width;
  private int height;

  @Inject
  public Terrains(EventBus eventBus, @BackingTerrainTable Table<Integer, Integer, Terrain> table) {
    super(eventBus);
    this.table = table;
    register();
  }

  @Subscribe
  @Override
  public void initialize(NewMap data) {
    this.width = data.getWidth();
    this.height = data.getHeight();
    for (Terrain terrain : data.getTerrains()) {
      Coordinate c = terrain.getCoordinate();
      table.put(c.getX(), c.getY(), terrain);
    }
    checkRectangular();
  }

  @Subscribe
  @Override
  public void dispose(ClearMap clearMap) {
    table.clear();
  }

  @Override
  public Iterator<Terrain> iterator() {
    return table.values().iterator();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Terrain get(Coordinate coordinate) {
    return table.get(coordinate.getX(), coordinate.getY());
  }

  public Iterable<Terrain> getAll(Iterable<Coordinate> coordinates) {
    return Iterables.transform(
        coordinates,
        new Function<Coordinate, Terrain>() {
          @Override
          public Terrain apply(Coordinate input) {
            return get(input);
          }
        });
  }

  private void checkRectangular() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Preconditions.checkNotNull(get(new Coordinate(x, y)), "Terrain grid must be rectangular");
      }
    }
  }

  @BindingAnnotation
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackingTerrainTable {

  }
}
