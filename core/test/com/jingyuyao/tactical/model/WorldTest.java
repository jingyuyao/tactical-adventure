package com.jingyuyao.tactical.model;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.terrain.Terrain;
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

  private BiMap<Coordinate, Cell> cellBiMap;
  private World world;

  @Before
  public void setUp() {
    cellBiMap = HashBiMap.create();
    world = new World(worldEventBus, mapState, cellBiMap);
  }

  @Test
  public void load() {
    Map<Coordinate, Cell> map = ImmutableMap.of(COORDINATE1, cell1, COORDINATE2, cell2);

    world.load(waiting, map);

    assertThat(cellBiMap).containsExactly(COORDINATE1, cell1, COORDINATE2, cell2);
    assertThat(world.getMaxHeight()).isEqualTo(COORDINATE1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(COORDINATE2.getX() + 1);
    verify(mapState).initialize(waiting);
    verify(worldEventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, map, WorldLoad.class);
  }

  @Test
  public void reset() {
    cellBiMap.put(COORDINATE1, cell1);

    world.reset();

    assertThat(cellBiMap).isEmpty();
    verify(worldEventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(WorldReset.class);
  }

  @Test
  public void get_coordinate() {
    Map<Coordinate, Cell> map = ImmutableMap.of(COORDINATE1, cell1, COORDINATE2, cell2);

    world.load(waiting, map);

    assertThat(world.getCoordinate(cell1)).isEqualTo(COORDINATE1);
    assertThat(world.getCoordinate(cell2)).isEqualTo(COORDINATE2);
  }

  @Test
  public void get_characters() {
    Map<Coordinate, Cell> map = ImmutableMap.of(COORDINATE1, cell1, COORDINATE2, cell2);
    when(cell1.hasCharacter()).thenReturn(true);
    when(cell1.getCharacter()).thenReturn(character1);
    when(cell2.hasCharacter()).thenReturn(true);
    when(cell2.getCharacter()).thenReturn(character2);

    world.load(waiting, map);

    assertThat(world.getCharacters()).containsExactly(character1, character2);
  }

  @Test
  public void get_terrains() {
    Map<Coordinate, Cell> map = ImmutableMap.of(COORDINATE1, cell1, COORDINATE2, cell2);
    when(cell1.hasTerrain()).thenReturn(true);
    when(cell1.getTerrain()).thenReturn(terrain1);
    when(cell2.hasTerrain()).thenReturn(true);
    when(cell2.getTerrain()).thenReturn(terrain2);

    world.load(waiting, map);

    assertThat(world.getTerrains()).containsExactly(terrain1, terrain2);
  }

  @Test
  public void no_neighbor() {
    Map<Coordinate, Cell> map = ImmutableMap.of(COORDINATE1, cell1, COORDINATE2, cell2);

    world.load(waiting, map);

    assertThat(world.getNeighbors(NO_NEIGHBOR)).isEmpty();
  }

  @Test
  public void get_neighbors_some() {
    Map<Coordinate, Cell> map = ImmutableMap.of(COORDINATE1, cell1, COORDINATE2, cell2);

    world.load(waiting, map);

    assertThat(world.getNeighbors(TWO_NEIGHBOR)).containsExactly(cell1, cell2);
  }

  @Test
  public void all_neighbors() {
    Map<Coordinate, Cell> map = ImmutableMap.of(
        COORDINATE1, cell1,
        COORDINATE2, cell2,
        COORDINATE3, cell3,
        COORDINATE4, cell4
    );

    world.load(waiting, map);

    assertThat(world.getNeighbors(FOUR_NEIGHBOR)).containsExactly(cell1, cell2, cell3, cell4);
  }

  @Test
  public void select() {
    cellBiMap.put(COORDINATE1, cell1);

    world.select(cell1);

    verify(mapState).select(COORDINATE1, cell1);
    verify(worldEventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(SelectCell.class);
    SelectCell selectCell = (SelectCell) argumentCaptor.getValue();
    assertThat(selectCell.getCell()).isSameAs(cell1);
    assertThat(selectCell.getCoordinate()).isEqualTo(COORDINATE1);
  }

  @Test
  public void move_character() {
    when(cell1.hasCharacter()).thenReturn(true);
    when(cell2.hasCharacter()).thenReturn(false);
    when(cell1.getCharacter()).thenReturn(character1);

    world.moveCharacter(cell1, cell2);

    verify(cell2).setCharacter(character1);
    verify(cell1).setCharacter(null);
  }
}
