package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.ship.Ship;
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
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    terrainMap.put(COORDINATE1, terrain1);
    terrainMap.put(COORDINATE2, terrain2);
    shipMap.put(COORDINATE1, ship1);
    shipMap.put(COORDINATE2, ship2);
    when(cellFactory.create(COORDINATE1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(COORDINATE2, terrain2)).thenReturn(cell2);
    when(cell1.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship1));
    when(cell2.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship2));

    world.initialize(terrainMap, shipMap);

    assertThat(cellMap).containsExactly(COORDINATE1, cell1, COORDINATE2, cell2);
    assertThat(world.getMaxHeight()).isEqualTo(COORDINATE1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(COORDINATE2.getX() + 1);
    verify(cell1).spawnShip(ship1);
    verify(cell2).spawnShip(ship2);
    verify(modelBus).post(argumentCaptor.capture());
    WorldLoaded worldLoaded = TestHelpers.assertClass(argumentCaptor.getValue(), WorldLoaded.class);
    assertThat(worldLoaded.getWorld()).isSameAs(world);
  }

  @Test
  public void reset() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    terrainMap.put(COORDINATE1, terrain1);
    terrainMap.put(COORDINATE2, terrain2);
    shipMap.put(COORDINATE1, ship1);
    shipMap.put(COORDINATE2, ship2);
    when(cellFactory.create(COORDINATE1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(COORDINATE2, terrain2)).thenReturn(cell2);
    when(cell1.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship1));
    when(cell2.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship2));

    world.initialize(terrainMap, shipMap);
    world.reset();

    assertThat(cellMap).isEmpty();
    assertThat(world.getMaxHeight()).isEqualTo(0);
    assertThat(world.getMaxWidth()).isEqualTo(0);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    WorldLoaded worldLoaded = TestHelpers.assertClass(argumentCaptor, 0, WorldLoaded.class);
    assertThat(worldLoaded.getWorld()).isSameAs(world);
    WorldReset worldReset = TestHelpers.assertClass(argumentCaptor, 1, WorldReset.class);
    assertThat(worldReset.getWorld()).isSameAs(world);
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
  public void get_ships() {
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));

    assertThat(world.getShipSnapshot()).containsExactly(cell1, ship1, cell2, ship2);
  }

  @Test
  public void make_all_player_ships_controllable() {
    cellMap.put(COORDINATE1, cell1);
    cellMap.put(COORDINATE2, cell2);
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.getAllegiance()).thenReturn(Allegiance.PLAYER);
    when(ship2.getAllegiance()).thenReturn(Allegiance.ENEMY);

    world.makeAllPlayerShipsControllable();

    verify(ship1).setControllable(true);
    verify(ship2, never()).setControllable(true);
  }
}
