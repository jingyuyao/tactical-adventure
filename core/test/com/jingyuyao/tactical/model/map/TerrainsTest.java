package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import com.jingyuyao.tactical.model.map.event.AddTerrain;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainsTest {

  private static final int WIDTH = 2;
  private static final int HEIGHT = 1;
  private static final Coordinate COORDINATE1 = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(1, 0);

  @Mock
  private EventBus eventBus;
  @Mock
  private Map<Coordinate, Terrain> terrainMap;
  @Mock
  private NewMap newMap;
  @Mock
  private ClearMap clearMap;
  @Mock
  private List<Terrain> terrainList;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private Collection<Terrain> terrainCollection;
  @Mock
  private Iterator<Terrain> terrainIterator;
  @Mock
  private Iterable<Coordinate> coordinateIterable;
  @Mock
  private Iterator<Coordinate> coordinateIterator;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Terrains terrains;

  @Before
  public void setUp() {
    terrains = new Terrains(eventBus, terrainMap);
  }

  @Test
  public void initialize() {
    when(terrainList.iterator()).thenReturn(terrainIterator);
    when(terrainIterator.hasNext()).thenReturn(true, true, false);
    when(terrainIterator.next()).thenReturn(terrain1, terrain2);
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);
    when(terrainMap.containsKey(COORDINATE1)).thenReturn(true);
    when(terrainMap.containsKey(COORDINATE2)).thenReturn(true);

    terrains.initialize(terrainList, WIDTH, HEIGHT);

    verify(terrainMap).put(COORDINATE1, terrain1);
    verify(terrainMap).put(COORDINATE2, terrain2);
    assertThat(terrains.getWidth()).isEqualTo(WIDTH);
    assertThat(terrains.getHeight()).isEqualTo(HEIGHT);
    verify(eventBus, times(2)).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues()).hasSize(2);
    assertThat(
        TestHelpers.isInstanceOf(
            argumentCaptor.getAllValues().get(0), AddTerrain.class).getObject())
        .isSameAs(terrain1);
    assertThat(
        TestHelpers.isInstanceOf(
            argumentCaptor.getAllValues().get(1), AddTerrain.class).getObject())
        .isSameAs(terrain2);
  }

  @Test(expected = IllegalStateException.class)
  public void initialize_not_fully_populated() {
    when(terrainList.iterator()).thenReturn(terrainIterator);
    when(terrainIterator.hasNext()).thenReturn(true, false);
    when(terrainIterator.next()).thenReturn(terrain1);
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrainMap.containsKey(COORDINATE1)).thenReturn(true);

    terrains.initialize(terrainList, WIDTH, HEIGHT);
  }

  @Test
  public void get() {
    when(terrainMap.get(COORDINATE1)).thenReturn(terrain1);
    when(terrainMap.get(COORDINATE2)).thenReturn(terrain2);

    assertThat(terrains.get(COORDINATE1)).isSameAs(terrain1);
    assertThat(terrains.get(COORDINATE2)).isSameAs(terrain2);
  }

  @Test
  public void getAll() {
    when(terrainMap.get(COORDINATE1)).thenReturn(terrain1);
    when(terrainMap.get(COORDINATE2)).thenReturn(terrain2);
    when(coordinateIterable.iterator()).thenReturn(coordinateIterator);
    when(coordinateIterator.next()).thenReturn(COORDINATE1, COORDINATE2);

    Iterable<Terrain> result = terrains.getAll(coordinateIterable);
    Iterator<Terrain> resultIterator = result.iterator();
    assertThat(resultIterator.next()).isSameAs(terrain1);
    assertThat(resultIterator.next()).isSameAs(terrain2);
  }

  @Test
  public void iterator() {
    when(terrainMap.values()).thenReturn(terrainCollection);
    when(terrainCollection.iterator()).thenReturn(terrainIterator);

    assertThat(terrains.iterator()).isSameAs(terrainIterator);
  }
}