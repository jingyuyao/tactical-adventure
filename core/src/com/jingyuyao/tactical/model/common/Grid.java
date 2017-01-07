package com.jingyuyao.tactical.model.common;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.Coordinate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A grid implementation backed by {@link ArrayList}s.
 *
 * @param <T> the data type stored in this grid
 */
public class Grid<T> implements Iterable<T> {

  private final List<List<T>> rows;
  private final int width;
  private final int height;

  public Grid(int width, int height) {
    Preconditions.checkArgument(width >= 0);
    Preconditions.checkArgument(height >= 0);

    this.width = width;
    this.height = height;
    rows = new ArrayList<List<T>>(height);
    for (int i = 0; i < height; i++) {
      List<T> row = new ArrayList<T>(width);
      for (int j = 0; j < width; j++) {
        row.add(null);
      }
      rows.add(row);
    }
  }

  public T get(int x, int y) {
    return rows.get(y).get(x);
  }

  public void set(int x, int y, T data) {
    rows.get(y).set(x, data);
  }

  public T push(int x, int y, T data) {
    T oldData = get(x, y);
    set(x, y, data);
    return oldData;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void validate() throws IllegalStateException {
    for (T obj : this) {
      try {
        Preconditions.checkNotNull(obj);
      } catch (NullPointerException e) {
        throw new IllegalStateException("Grid is not fully populated");
      }
    }
  }

  /**
   * Creates a {@link Grid} with the given {@code width} and {@code height} and populate it
   * with {@code object} with {@code coordinateExtractor} providing the {@link Coordinate} for
   * each object.
   */
  public static <T> Grid<T> from(
      int width, int height, Iterable<T> objects, Function<T, Coordinate> coordinateExtractor) {
    Grid<T> grid = new Grid<T>(width, height);

    for (T obj : objects) {
      Coordinate c = coordinateExtractor.apply(obj);
      Preconditions.checkNotNull(c);
      grid.set(c.getX(), c.getY(), obj);
    }

    grid.validate();
    return grid;
  }

  @Override
  public Iterator<T> iterator() {
    return Iterables.concat(rows).iterator();
  }
}
