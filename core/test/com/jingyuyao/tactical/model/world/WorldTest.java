package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C1_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C1_1;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C1_2;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C2_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C2_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
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

  @Mock
  private ModelBus modelBus;
  @Mock
  private Dijkstra dijkstra;
  @Mock
  private CellFactory cellFactory;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Cell cell3;
  @Mock
  private Cell cell4;
  @Mock
  private Cell cell5;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private ValueGraph<Cell, Integer> graph;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<GetEdgeCost> edgeCostCaptor;

  private Map<Coordinate, Cell> cellMap;
  private World world;

  @Before
  public void setUp() {
    cellMap = new HashMap<>();
    world = new World(modelBus, dijkstra, cellFactory, cellMap);
  }

  @Test
  public void initialize() {
    Map<Coordinate, Terrain> terrainMap = new HashMap<>();
    Map<Coordinate, Ship> shipMap = new HashMap<>();
    terrainMap.put(C1_1, terrain1);
    terrainMap.put(C2_0, terrain2);
    shipMap.put(C1_1, ship1);
    shipMap.put(C2_0, ship2);
    when(cell1.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship1));
    when(cell2.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship2));
    when(cell1.getTerrain()).thenReturn(terrain1);
    when(cell2.getTerrain()).thenReturn(terrain2);
    when(terrain1.canHold(ship1)).thenReturn(true);
    when(terrain2.canHold(ship2)).thenReturn(true);
    when(cellFactory.create(C1_1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(C2_0, terrain2)).thenReturn(cell2);

    world.initialize(terrainMap, shipMap);

    assertThat(cellMap).containsExactly(C1_1, cell1, C2_0, cell2);
    assertThat(world.getMaxHeight()).isEqualTo(C1_1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(C2_0.getX() + 1);
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
    terrainMap.put(C1_1, terrain1);
    terrainMap.put(C2_0, terrain2);
    shipMap.put(C1_1, ship1);
    shipMap.put(C2_0, ship2);
    when(cell1.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship1));
    when(cell2.ship())
        .thenReturn(Optional.<Ship>absent()).thenReturn(Optional.of(ship2));
    when(cell1.getTerrain()).thenReturn(terrain1);
    when(cell2.getTerrain()).thenReturn(terrain2);
    when(terrain1.canHold(ship1)).thenReturn(true);
    when(terrain2.canHold(ship2)).thenReturn(true);
    when(cellFactory.create(C1_1, terrain1)).thenReturn(cell1);
    when(cellFactory.create(C2_0, terrain2)).thenReturn(cell2);

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
    cellMap.put(C1_1, cell1);

    assertThat(world.cell(C1_1.getX(), C1_1.getY())).hasValue(cell1);
  }

  @Test
  public void get_cell_coordinate() {
    cellMap.put(C1_1, cell1);

    assertThat(world.cell(C1_1)).hasValue(cell1);
  }

  @Test
  public void get_neighbors() {
    cellMap.put(C1_1, cell1);
    cellMap.put(C1_2, cell2); // up
    cellMap.put(C1_0, cell3); // down
    cellMap.put(C0_1, cell4); // left
    cellMap.put(C2_1, cell5); // right
    when(cell1.getCoordinate()).thenReturn(C1_1);

    assertThat(world.getNeighbors(cell1)).containsExactly(cell2, cell3, cell4, cell5);
  }

  @Test
  public void neighbor() {
    cellMap.put(C1_1, cell1);
    cellMap.put(C1_2, cell2); // up
    cellMap.put(C1_0, cell3); // down
    cellMap.put(C0_1, cell4); // left
    cellMap.put(C2_1, cell5); // right
    when(cell1.getCoordinate()).thenReturn(C1_1);

    assertThat(world.neighbor(cell1, Direction.UP)).hasValue(cell2);
    assertThat(world.neighbor(cell1, Direction.DOWN)).hasValue(cell3);
    assertThat(world.neighbor(cell1, Direction.LEFT)).hasValue(cell4);
    assertThat(world.neighbor(cell1, Direction.RIGHT)).hasValue(cell5);
  }

  @Test
  public void get_ship_movement() {
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(ship1.getMoveDistance()).thenReturn(5);
    when(dijkstra.minPathSearch(eq(world), any(GetEdgeCost.class), eq(cell1), eq(5)))
        .thenReturn(graph);

    Movement movement = world.getShipMovement(cell1);

    assertThat(movement.getMoveGraph()).isSameAs(graph);
    verify(dijkstra).minPathSearch(eq(world), edgeCostCaptor.capture(), eq(cell1), eq(5));
    assertThat(edgeCostCaptor.getValue()).isInstanceOf(ShipCost.class);
  }

  @Test
  public void get_unimpeded_movement() {
    when(dijkstra.minPathSearch(eq(world), any(GetEdgeCost.class), eq(cell1), eq(5)))
        .thenReturn(graph);

    Movement movement = world.getUnimpededMovement(cell1, 5);

    assertThat(movement.getMoveGraph()).isSameAs(graph);
    verify(dijkstra).minPathSearch(eq(world), edgeCostCaptor.capture(), eq(cell1), eq(5));
    assertThat(edgeCostCaptor.getValue()).isInstanceOf(OneCost.class);
  }

  @Test
  public void get_ships() {
    cellMap.put(C1_1, cell1);
    cellMap.put(C2_0, cell2);
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));

    assertThat(world.getShipSnapshot()).containsExactly(cell1, ship1, cell2, ship2);
  }

  @Test
  public void make_all_player_ships_controllable() {
    cellMap.put(C1_1, cell1);
    cellMap.put(C2_0, cell2);
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);

    world.makeAllPlayerShipsControllable();

    verify(ship1).setControllable(true);
    verify(ship2, never()).setControllable(true);
  }
}
