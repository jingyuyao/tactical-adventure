package com.jingyuyao.tactical.model.common;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.Coordinate;
import java.util.Iterator;
import java.util.Map;

/**
 * A "grid" of {@link Coordinate} to object mapping.
 *
 * @param <T> the data type stored in this grid
 */
// TODO: we can consider adding functions like swap
public class Grid<T> implements Iterable<T> {

  // We rely on coordinates' hashing invariant
  private final Map<Coordinate, T> coordinateMap;
  private final int width;
  private final int height;

  /**
   * Creates a {@link Grid} with the given {@code width}, {@code height} and {@code coordinateMap}.
   * The resulting {@link Grid} is guaranteed to be rectangular.
   */
  public Grid(int width, int height, Map<Coordinate, T> coordinateMap) {
    Preconditions.checkArgument(width >= 0);
    Preconditions.checkArgument(height >= 0);

    this.width = width;
    this.height = height;
    this.coordinateMap = coordinateMap;
    validateRectangular();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public T get(Coordinate coordinate) {
    return coordinateMap.get(coordinate);
  }

  public Iterable<T> getAll(Iterable<Coordinate> coordinates) {
    return Iterables.transform(coordinates, new Function<Coordinate, T>() {
      @Override
      public T apply(Coordinate input) {
        return get(input);
      }
    });
  }

  @Override
  public Iterator<T> iterator() {
    return coordinateMap.values().iterator();
  }

  private void validateRectangular() throws IllegalStateException {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (!coordinateMap.containsKey(new Coordinate(x, y))) {
          throw new IllegalStateException("Grid is not fully populated");
        }
      }
    }
  }
}
