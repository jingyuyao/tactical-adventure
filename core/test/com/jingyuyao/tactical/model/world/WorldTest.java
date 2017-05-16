package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.Ship;
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

  @Mock
  private ModelBus modelBus;
  @Mock
  private CellFactory cellFactory;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
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
    Map<Coordinate, Ship> characterMap = new HashMap<>();
    terrainMap.put(COORDINATE1, terrain1);
    terrainMap.put(COORDINATE2, terrain2);
    characterMap.put(COORDINATE1, ship1);
    characterMap.put(COORDINATE2, ship2);
    when(cellFactory.create(COORDINATE1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(COORDINATE2, terrain2)).thenReturn(cell2);
    when(cell1.character())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship1));
    when(cell2.character())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship2));

    world.initialize(terrainMap, characterMap);

    assertThat(cellMap).containsExactly(COORDINATE1, cell1, COORDINATE2, cell2);
    assertThat(world.getMaxHeight()).isEqualTo(COORDINATE1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(COORDINATE2.getX() + 1);
    verify(cell1).spawnCharacter(ship1);
    verify(cell2).spawnCharacter(ship2);
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cellMap.values(), WorldLoad.class);
  }

  @Test
  public void reset() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> characterMap = new HashMap<>();
    terrainMap.put(COORDINATE1, terrain1);
    terrainMap.put(COORDINATE2, terrain2);
    characterMap.put(COORDINATE1, ship1);
    characterMap.put(COORDINATE2, ship2);
    when(cellFactory.create(COORDINATE1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(COORDINATE2, terrain2)).thenReturn(cell2);
    when(cell1.character())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship1));
    when(cell2.character())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship2));

    world.initialize(terrainMap, characterMap);
    world.reset();

    assertThat(cellMap).isEmpty();
    assertThat(world.getMaxHeight()).isEqualTo(0);
    assertThat(world.getMaxWidth()).isEqualTo(0);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cellMap.values(), WorldLoad.class);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(WorldReset.class);
  }

  @Test
  public void get_character_snapshot() {
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);
    when(cell1.character()).thenReturn(Optional.of(ship1));
    when(cell2.character()).thenReturn(Optional.of(ship2));

    assertThat(world.getCharacterSnapshot()).containsExactly(cell1, cell2);
  }

  @Test
  public void get_cell() {
    cellMap.put(COORDINATE1, cell1);

    assertThat(world.cell(COORDINATE1.getX(), COORDINATE1.getY())).hasValue(cell1);
  }

  @Test
  public void get_cell_coordinate() {
    cellMap.put(COORDINATE1, cell1);

    assertThat(world.cell(COORDINATE1)).hasValue(cell1);
  }

  @Test
  public void full_heal_players() {
    when(cell1.player()).thenReturn(Optional.of(player));
    when(cell2.player()).thenReturn(Optional.<Player>absent());
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);

    world.fullHealPlayers();

    verify(player).fullHeal();
  }
}
