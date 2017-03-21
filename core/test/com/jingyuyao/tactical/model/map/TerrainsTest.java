package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.event.AddTerrain;
import com.jingyuyao.tactical.model.event.RemoveObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

  private static final Coordinate COORDINATE1 = new Coordinate(1, 1);
  private static final Coordinate COORDINATE2 = new Coordinate(2, 0);
  private static final Coordinate COORDINATE3 = new Coordinate(3, 1);
  private static final Coordinate COORDINATE4 = new Coordinate(2, 2);
  private static final Coordinate TWO_NEIGHBOR = new Coordinate(1, 0);
  private static final Coordinate FOUR_NEIGHBOR = new Coordinate(2, 1);
  private static final Coordinate NO_NEIGHBOR = new Coordinate(100, 100);

  @Mock
  private EventBus eventBus;
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

  private Map<Coordinate, Terrain> terrainMap;
  private Terrains terrains;

  @Before
  public void setUp() {
    terrainMap = new LinkedHashMap<>(); // preserves insertion order for testing
    terrains = new Terrains(eventBus, terrainMap);
  }

  @Test
  public void add() {
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);

    terrains.add(terrain2);

    assertThat(terrainMap).containsExactly(COORDINATE2, terrain2);
    assertThat(terrains.getMaxWidth()).isEqualTo(COORDINATE2.getX());
    assertThat(terrains.getMaxHeight()).isEqualTo(COORDINATE2.getY());
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, terrain2, AddTerrain.class);
  }

  @Test
  public void getAll() {
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);
    terrains.add(terrain1);
    terrains.add(terrain2);

    Iterable<Terrain> result = terrains.getAll(ImmutableList.of(COORDINATE1, COORDINATE2));
    Iterator<Terrain> resultIterator = result.iterator();
    assertThat(resultIterator.next()).isSameAs(terrain1);
    assertThat(resultIterator.next()).isSameAs(terrain2);
  }

  @Test
  public void contains() {
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    terrains.add(terrain1);

    assertThat(terrains.contains(COORDINATE1)).isTrue();
  }

  @Test
  public void no_neighbor() {
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);
    terrains.add(terrain1);
    terrains.add(terrain2);

    assertThat(terrains.getNeighbors(NO_NEIGHBOR)).isEmpty();
  }

  @Test
  public void get_neighbors_some() {
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);
    terrains.add(terrain1);
    terrains.add(terrain2);

    assertThat(terrains.getNeighbors(TWO_NEIGHBOR)).containsExactly(terrain1, terrain2);
  }

  @Test
  public void all_neighbors() {
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);
    when(terrain3.getCoordinate()).thenReturn(COORDINATE3);
    when(terrain4.getCoordinate()).thenReturn(COORDINATE4);
    terrains.add(terrain1);
    terrains.add(terrain2);
    terrains.add(terrain3);
    terrains.add(terrain4);

    assertThat(terrains.getNeighbors(FOUR_NEIGHBOR))
        .containsExactly(terrain1, terrain2, terrain3, terrain4);
  }

  @Test
  public void reset() {
    when(terrain1.getCoordinate()).thenReturn(COORDINATE1);
    when(terrain2.getCoordinate()).thenReturn(COORDINATE2);
    terrains.add(terrain1);
    terrains.add(terrain2);

    terrains.reset();

    verify(eventBus, times(4)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, terrain1, AddTerrain.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, terrain2, AddTerrain.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, terrain1, RemoveObject.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 3, terrain2, RemoveObject.class);
    assertThat(terrainMap).isEmpty();
  }
}