package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
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
  private ModelBus modelBus;
  @Mock
  private CellFactory cellFactory;
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
  @Mock
  private Player player;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Map<Coordinate, Cell> cellMap;
  private World world;

  @Before
  public void setUp() {
    cellMap = new HashMap<>();
    world = new World(modelBus, cellFactory, cellMap);
  }

  @Test
  public void initialize() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Character> characterMap = new HashMap<>();
    terrainMap.put(COORDINATE1, terrain1);
    terrainMap.put(COORDINATE2, terrain2);
    characterMap.put(COORDINATE1, character1);
    characterMap.put(COORDINATE2, character2);
    when(cellFactory.create(COORDINATE1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(COORDINATE2, terrain2)).thenReturn(cell2);
    when(cell1.hasCharacter()).thenReturn(false, true);
    when(cell2.hasCharacter()).thenReturn(false, true);

    world.initialize(terrainMap, characterMap);

    assertThat(cellMap).containsExactly(COORDINATE1, cell1, COORDINATE2, cell2);
    assertThat(world.getMaxHeight()).isEqualTo(COORDINATE1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(COORDINATE2.getX() + 1);
    verify(cell1).spawnCharacter(character1);
    verify(cell2).spawnCharacter(character2);
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cellMap.values(), WorldLoad.class);
  }

  @Test
  public void reset() {
    cellMap.put(COORDINATE1, cell1);

    world.reset();

    assertThat(cellMap).isEmpty();
    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(WorldReset.class);
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
  public void get_cell() {
    cellMap.put(COORDINATE1, cell1);

    assertThat(world.getCell(COORDINATE1.getX(), COORDINATE1.getY())).hasValue(cell1);
  }

  @Test
  public void no_neighbor() {
    when(temp.getCoordinate()).thenReturn(NO_NEIGHBOR);
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);

    assertThat(world.getNeighbors(temp)).isEmpty();
  }

  @Test
  public void get_neighbors_some() {
    when(temp.getCoordinate()).thenReturn(TWO_NEIGHBOR);
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);

    assertThat(world.getNeighbors(temp)).containsExactly(cell1, cell2);
  }

  @Test
  public void all_neighbors() {
    when(temp.getCoordinate()).thenReturn(FOUR_NEIGHBOR);
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);
    cellMap.put(COORDINATE3, cell3);
    cellMap.put(COORDINATE4, cell4);

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
  public void full_heal_players() {
    when(cell1.hasPlayer()).thenReturn(true);
    when(cell1.getPlayer()).thenReturn(player);
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);

    world.fullHealPlayers();

    verify(player).fullHeal();
  }
}
