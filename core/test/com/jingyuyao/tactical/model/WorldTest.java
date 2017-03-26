package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Direction;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldTest {

  private static final Coordinate COORDINATE1 = new Coordinate(1, 1);
  private static final Coordinate COORDINATE2 = new Coordinate(2, 0);
  private static final Coordinate COORDINATE3 = new Coordinate(3, 1);
  private static final Coordinate COORDINATE4 = new Coordinate(2, 2);
  private static final Coordinate TWO_NEIGHBOR = new Coordinate(1, 0);
  private static final Coordinate FOUR_NEIGHBOR = new Coordinate(2, 1);
  private static final Coordinate NO_NEIGHBOR = new Coordinate(100, 100);

  @Mock
  private EventBus worldEventBus;
  @Mock
  private MapState mapState;
  @Mock
  private Waiting waiting;
  @Mock
  private Cell temp;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Cell cell3;
  @Mock
  private Cell cell4;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private Character character1;
  @Mock
  private Character character2;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Map<Coordinate, Cell> cellMap;
  private World world;

  @Before
  public void setUp() {
    cellMap = new HashMap<>();
    world = new World(worldEventBus, mapState, cellMap);

    when(cell1.getCoordinate()).thenReturn(COORDINATE1);
    when(cell2.getCoordinate()).thenReturn(COORDINATE2);
    when(cell3.getCoordinate()).thenReturn(COORDINATE3);
    when(cell4.getCoordinate()).thenReturn(COORDINATE4);
    when(cell1.hasCharacter()).thenReturn(true);
    when(cell2.hasCharacter()).thenReturn(true);
  }

  @Test
  public void load() {
    Iterable<Cell> list = ImmutableList.of(cell1, cell2);
    world.load(waiting, list);

    assertThat(cellMap).containsExactly(COORDINATE1, cell1, COORDINATE2, cell2);
    assertThat(world.getMaxHeight()).isEqualTo(COORDINATE1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(COORDINATE2.getX() + 1);
    verify(mapState).initialize(waiting);
    verify(worldEventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, list, WorldLoad.class);
  }

  @Test
  public void prep_for_save() {
    world.prepForSave();

    verify(mapState).prepForSave();
  }

  @Test
  public void reset() {
    cellMap.put(COORDINATE1, cell1);

    world.reset();

    assertThat(cellMap).isEmpty();
    verify(worldEventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(WorldReset.class);
  }

  @Test
  public void get_cells() {
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);

    assertThat(world.getCells()).containsExactly(cell1, cell2);
  }

  @Test
  public void get_character_snapshot() {
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);
    when(cell1.hasCharacter()).thenReturn(true);
    when(cell2.hasCharacter()).thenReturn(true);

    assertThat(world.getCharacterSnapshot()).containsExactly(cell1, cell2);
  }

  @Test
  public void no_neighbor() {
    when(temp.getCoordinate()).thenReturn(NO_NEIGHBOR);

    world.load(waiting, ImmutableList.of(cell1, cell2));

    assertThat(world.getNeighbors(temp)).isEmpty();
  }

  @Test
  public void get_neighbors_some() {
    when(temp.getCoordinate()).thenReturn(TWO_NEIGHBOR);

    world.load(waiting, ImmutableList.of(cell1, cell2));

    assertThat(world.getNeighbors(temp)).containsExactly(cell1, cell2);
  }

  @Test
  public void all_neighbors() {
    when(temp.getCoordinate()).thenReturn(FOUR_NEIGHBOR);

    world.load(waiting, ImmutableList.of(cell1, cell2, cell3, cell4));

    assertThat(world.getNeighbors(temp)).containsExactly(cell1, cell2, cell3, cell4);
  }

  @Test
  public void get_neighbor() {
    Coordinate from = new Coordinate(5, 5);
    Coordinate up = from.offsetBy(Direction.UP);
    when(temp.getCoordinate()).thenReturn(from);
    cellMap.put(up, cell1);

    assertThat(world.getNeighbor(temp, Direction.UP)).hasValue(cell1);
    assertThat(world.getNeighbor(temp, Direction.DOWN)).isAbsent();
  }

  @Test
  public void select() {
    cellMap.put(COORDINATE1, cell1);

    world.select(cell1);

    verify(mapState).select(cell1);
    verify(worldEventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell1, SelectCell.class);
  }
}
