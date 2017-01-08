package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.common.Grid;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainsTest {

  private static final int WIDTH = 10;
  private static final int HEIGHT = 11;
  private static final Coordinate COORDINATE = new Coordinate(0, 0);

  @Mock
  private EventBus eventBus;
  @Mock
  private Grid<Terrain> grid;
  @Mock
  private NewMap newMap;
  @Mock
  private ClearMap clearMap;
  @Mock
  private Terrain terrain;
  @Mock
  private Iterable<Terrain> terrainIterable;
  @Mock
  private Iterable<Coordinate> coordinateIterable;
  @Mock
  private Iterator<Terrain> terrainIterator;

  private Terrains terrains;

  @Before
  public void setUp() {
    terrains = new Terrains(eventBus);
    verify(eventBus).register(terrains);
    initialize_and_link();
  }

  @Test
  public void dispose() {
    terrains.dispose(clearMap);

    try {
      terrains.getWidth();
      fail();
    } catch (NullPointerException ignored) {
      // should error out at getWidth()
    }
  }

  @Test
  public void width_and_height() {
    when(grid.getWidth()).thenReturn(WIDTH);
    when(grid.getHeight()).thenReturn(HEIGHT);

    assertThat(terrains.getWidth()).isEqualTo(WIDTH);
    assertThat(terrains.getHeight()).isEqualTo(HEIGHT);
  }

  @Test
  public void get() {
    when(grid.get(COORDINATE)).thenReturn(terrain);

    assertThat(terrains.get(COORDINATE)).isSameAs(terrain);
  }

  @Test
  public void getAll() {
    when(grid.getAll(coordinateIterable)).thenReturn(terrainIterable);

    assertThat(terrains.getAll(coordinateIterable)).isSameAs(terrainIterable);
  }

  @Test
  public void iterator() {
    when(grid.iterator()).thenReturn(terrainIterator);

    assertThat(terrains.iterator()).isSameAs(terrainIterator);
  }

  private void initialize_and_link() {
    when(newMap.getTerrainGrid()).thenReturn(grid);
    terrains.initialize(newMap);
  }
}