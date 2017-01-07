package com.jingyuyao.tactical.model.common;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.jingyuyao.tactical.model.Coordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class GridTest {

  private static final int WIDTH = 3;
  private static final int HEIGHT = 2;
  private static final int SIZE = WIDTH * HEIGHT;

  private Grid<Coordinate> grid;

  @Before
  public void setUp() {
    grid = new Grid<Coordinate>(WIDTH, HEIGHT);
  }

  @Test(expected = IllegalStateException.class)
  public void validate_nulls() {
    grid.validate();
  }

  @Test(expected = IllegalStateException.class)
  public void validate_single_null() {
    grid.set(0, 0, obj(0, 0));
    grid.set(0, 1, obj(0, 1));
    grid.set(1, 0, obj(1, 0));
    grid.set(1, 1, obj(1, 1));
    grid.set(2, 0, obj(2, 0));
    grid.validate();
  }

  @Test
  public void get_and_set() {
    populateGrid();

    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        Coordinate c = grid.get(x, y);
        assertThat(c.getX()).isEqualTo(x);
        assertThat(c.getY()).isEqualTo(y);
      }
    }
  }

  @Test
  public void iteration() {
    populateGrid();

    Set<Coordinate> coordinates = new HashSet<Coordinate>(SIZE);
    for (Coordinate c : grid) {
      if (coordinates.contains(c)) {
        fail("iteration contains duplicates");
      }
      coordinates.add(c);
    }

    assertThat(coordinates).hasSize(SIZE);
  }

  @Test
  public void push() {
    populateGrid();

    Coordinate newObj = obj(100, 100);
    Coordinate oldObj = grid.push(2, 1, newObj);

    assertThat(oldObj.getX()).isEqualTo(2);
    assertThat(oldObj.getY()).isEqualTo(1);
    assertThat(grid.get(2, 1)).isSameAs(newObj);
  }

  @Test
  public void from() {
    List<Coordinate> coordinates = new ArrayList<Coordinate>();
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        coordinates.add(obj(x, y));
      }
    }
    Function<Coordinate, Coordinate> extractor = Functions.identity();

    grid = Grid.from(WIDTH, HEIGHT, coordinates, extractor);

    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        Coordinate c = grid.get(x, y);
        assertThat(c.getX()).isEqualTo(x);
        assertThat(c.getY()).isEqualTo(y);
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void from_bad_inputs() {
    List<Coordinate> coordinates = new ArrayList<Coordinate>();
    Function<Coordinate, Coordinate> extractor = Functions.identity();

    Grid.from(WIDTH, HEIGHT, coordinates, extractor);
  }

  private void populateGrid() {
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        grid.set(x, y, obj(x, y));
      }
    }
  }

  private Coordinate obj(int x, int y) {
    return new Coordinate(x, y);
  }
}