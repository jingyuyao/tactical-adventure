package com.jingyuyao.tactical.model.common;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.Coordinate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GridTest {

  private static final int WIDTH = 2;
  private static final int HEIGHT = 3;

  @Mock
  private Map<Coordinate, Coordinate> coordinateMap;
  @Mock
  private Collection<Coordinate> coordinates;
  @Mock
  private Iterator<Coordinate> iterator;

  private Grid<Coordinate> grid;

  @Before
  public void setUp() {
    set_up_coordinate_map();
    grid = new Grid<Coordinate>(WIDTH, HEIGHT, coordinateMap);
  }

  @Test(expected = IllegalStateException.class)
  public void not_properly_populated() {
    when(coordinateMap.containsKey(coordinate(0, 1))).thenReturn(false);
    grid = new Grid<Coordinate>(WIDTH, HEIGHT, coordinateMap);
  }

  @Test
  public void get() {
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        assertThat(grid.get(coordinate(x, y))).isEqualTo(coordinate(x, y));
      }
    }
  }

  @Test
  public void iteration() {
    when(coordinateMap.values()).thenReturn(coordinates);
    when(coordinates.iterator()).thenReturn(iterator);

    // no point testing whether map's iterator works or not
    assertThat(grid.iterator()).isSameAs(iterator);
  }

  private void set_up_coordinate_map() {
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        Coordinate coordinate = coordinate(x, y);
        when(coordinateMap.containsKey(coordinate)).thenReturn(true);
        when(coordinateMap.get(coordinate)).thenReturn(coordinate);
      }
    }
  }

  private Coordinate coordinate(int x, int y) {
    return new Coordinate(x, y);
  }
}