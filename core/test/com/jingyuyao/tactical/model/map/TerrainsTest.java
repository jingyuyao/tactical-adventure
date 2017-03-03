package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.event.AddTerrain;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Iterator;
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

  private static final Coordinate COORDINATE1 = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(10, 11);

  @Mock
  private EventBus eventBus;
  @Mock
  private Map<Coordinate, Terrain> terrainMap;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private Terrain terrain3;
  @Mock
  private Terrain terrain4;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Terrains terrains;

  @Before
  public void setUp() {
    terrains = new Terrains(eventBus, terrainMap);
  }

  @Test
  public void add() {
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);

    terrains.add(terrain2);

    verify(terrainMap).put(COORDINATE2, terrain2);
    assertThat(terrains.getMaxWidth()).isEqualTo(COORDINATE2.getX());
    assertThat(terrains.getMaxHeight()).isEqualTo(COORDINATE2.getY());
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, terrain2, AddTerrain.class);
  }

  @Test
  public void getAll() {
    when(terrainMap.get(COORDINATE1)).thenReturn(terrain1);
    when(terrainMap.get(COORDINATE2)).thenReturn(terrain2);

    Iterable<Terrain> result = terrains.getAll(ImmutableList.of(COORDINATE1, COORDINATE2));
    Iterator<Terrain> resultIterator = result.iterator();
    assertThat(resultIterator.next()).isSameAs(terrain1);
    assertThat(resultIterator.next()).isSameAs(terrain2);
  }

  @Test
  public void contains() {
    when(terrainMap.containsKey(COORDINATE1)).thenReturn(true);

    assertThat(terrains.contains(COORDINATE1)).isTrue();
  }

  @Test
  public void get_neighbors_some() {
    when(terrainMap.containsKey(Directions.DOWN)).thenReturn(true);
    when(terrainMap.containsKey(Directions.UP)).thenReturn(true);
    when(terrainMap.get(Directions.DOWN)).thenReturn(terrain1);
    when(terrainMap.get(Directions.UP)).thenReturn(terrain2);

    assertThat(terrains.getNeighbors(COORDINATE1)).containsExactly(terrain1, terrain2);
  }

  @Test
  public void get_neighbors_all() {
    Map<Coordinate, Terrain> neighbors = ImmutableMap.of(
        Directions.DOWN, terrain1,
        Directions.LEFT, terrain2,
        Directions.RIGHT, terrain3,
        Directions.UP, terrain4
    );
    for (Coordinate direction : Directions.ALL) {
      when(terrainMap.containsKey(direction)).thenReturn(true);
      when(terrainMap.get(direction)).thenReturn(neighbors.get(direction));
    }

    assertThat(terrains.getNeighbors(COORDINATE1))
        .containsExactly(terrain1, terrain2, terrain3, terrain4);
  }
}