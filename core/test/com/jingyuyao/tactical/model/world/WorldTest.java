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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.graph.ValueGraph;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
public class WorldTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private Dijkstra dijkstra;
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
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private Path path;
  @Mock
  private ValueGraph<Cell, Integer> graph;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<GetEdgeCost> edgeCostCaptor;

  private Map<Coordinate, Cell> cellMap;
  private List<Ship> inactiveShips;
  private World world;

  @Before
  public void setUp() {
    cellMap = new HashMap<>();
    inactiveShips = new ArrayList<>();
    world = new World(modelBus, dijkstra, cellMap, inactiveShips);
  }

  @Test
  public void initialize() {
    when(cell1.getCoordinate()).thenReturn(C1_1);
    when(cell2.getCoordinate()).thenReturn(C2_0);

    world.initialize(2, Arrays.asList(cell1, cell2), Collections.singletonList(ship1));

    assertThat(world.cell(C1_1)).hasValue(cell1);
    assertThat(world.cell(C2_0)).hasValue(cell2);
    assertThat(world.getLevel()).isEqualTo(2);
    assertThat(world.getMaxHeight()).isEqualTo(C1_1.getY() + 1);
    assertThat(world.getMaxWidth()).isEqualTo(C2_0.getX() + 1);
    assertThat(world.getInactiveShips()).containsExactly(ship1);
    verify(modelBus).post(argumentCaptor.capture());
    WorldLoaded worldLoaded = TestHelpers.assertClass(argumentCaptor.getValue(), WorldLoaded.class);
    assertThat(worldLoaded.getWorld()).isSameAs(world);
  }

  @Test
  public void reset() {
    when(cell1.getCoordinate()).thenReturn(C1_1);
    when(cell2.getCoordinate()).thenReturn(C2_0);

    world.initialize(2, Arrays.asList(cell1, cell2), Collections.singletonList(ship1));
    world.reset();

    assertThat(world.cell(C1_1)).isAbsent();
    assertThat(world.cell(C2_0)).isAbsent();
    assertThat(world.getLevel()).isEqualTo(0);
    assertThat(world.getMaxHeight()).isEqualTo(0);
    assertThat(world.getMaxWidth()).isEqualTo(0);
    assertThat(world.getInactiveShips()).isEmpty();
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.assertClass(argumentCaptor, 0, WorldLoaded.class);
    WorldReset worldReset = TestHelpers.assertClass(argumentCaptor, 1, WorldReset.class);
    assertThat(worldReset.getWorld()).isSameAs(world);
  }

  @Test
  public void get_world_cells() {
    cellMap.put(C1_1, cell1);
    cellMap.put(C1_2, cell2);

    assertThat(world.getWorldCells()).containsExactly(cell1, cell2);
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
    assertThat(edgeCostCaptor.getValue()).isInstanceOf(TerrainCost.class);
  }

  @Test
  public void get_ship_movement_with_multiplier() {
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(ship1.getMoveDistance()).thenReturn(5);
    when(dijkstra.minPathSearch(eq(world), any(GetEdgeCost.class), eq(cell1), eq(7)))
        .thenReturn(graph);

    Movement movement = world.getShipMovement(cell1, 1.5f);

    assertThat(movement.getMoveGraph()).isSameAs(graph);
    verify(dijkstra).minPathSearch(eq(world), edgeCostCaptor.capture(), eq(cell1), eq(7));
    assertThat(edgeCostCaptor.getValue()).isInstanceOf(TerrainCost.class);
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
  public void get_active_ships() {
    cellMap.put(C1_1, cell1);
    cellMap.put(C2_0, cell2);
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));

    assertThat(world.getActiveShips()).containsExactly(cell1, ship1, cell2, ship2);
  }

  @Test
  public void get_inactive_ships() {
    inactiveShips.add(ship1);
    inactiveShips.add(ship2);

    assertThat(world.getInactiveShips()).containsExactly(ship1, ship2);
    assertThat(world.getInactiveShips()).isNotSameAs(inactiveShips); // return a copy
  }

  @Test
  public void spawn_ship() {
    world.spawnShip(cell1, ship1);

    verify(cell1).addShip(ship1);
    verify(modelBus).post(argumentCaptor.capture());
    SpawnShip spawnShip = TestHelpers.assertClass(argumentCaptor.getValue(), SpawnShip.class);
    assertThat(spawnShip.getObject()).isSameAs(cell1);
  }

  @Test
  public void remove_ship() {
    when(cell1.removeShip()).thenReturn(ship1);

    assertThat(world.removeShip(cell1)).isSameAs(ship1);

    verify(cell1).removeShip();
    verify(modelBus).post(argumentCaptor.capture());
    RemoveShip removeShip = TestHelpers.assertClass(argumentCaptor.getValue(), RemoveShip.class);
    assertThat(removeShip.getObject()).isSameAs(ship1);
  }

  @Test
  public void move_ship_moved() {
    when(cell1.moveShip(cell2)).thenReturn(Optional.of(ship1));

    world.moveShip(cell1, cell2);

    verify(cell1).moveShip(cell2);
    verify(modelBus).post(argumentCaptor.capture());
    InstantMoveShip instantMoveShip =
        TestHelpers.assertClass(argumentCaptor.getValue(), InstantMoveShip.class);
    assertThat(instantMoveShip.getShip()).isSameAs(ship1);
    assertThat(instantMoveShip.getDestination()).isSameAs(cell2);
  }

  @Test
  public void move_ship_not_moved() {
    when(cell1.moveShip(cell2)).thenReturn(Optional.<Ship>absent());

    world.moveShip(cell1, cell2);

    verify(cell1).moveShip(cell2);
    verifyZeroInteractions(modelBus);
  }

  @Test
  public void move_ship_path_moved() {
    when(path.getOrigin()).thenReturn(cell1);
    when(path.getDestination()).thenReturn(cell2);
    when(cell1.moveShip(cell2)).thenReturn(Optional.of(ship1));

    world.moveShip(path);

    verify(cell1).moveShip(cell2);
    verify(modelBus).post(argumentCaptor.capture());
    MoveShip moveShip = TestHelpers.assertClass(argumentCaptor.getValue(), MoveShip.class);
    assertThat(moveShip.getPath()).isSameAs(path);
    assertThat(moveShip.getShip()).isSameAs(ship1);
    assertThat(moveShip.getPromise().isDone()).isFalse();
  }

  @Test
  public void move_ship_path_not_moved() {
    when(path.getOrigin()).thenReturn(cell1);
    when(path.getDestination()).thenReturn(cell2);
    when(cell1.moveShip(cell2)).thenReturn(Optional.<Ship>absent());

    world.moveShip(path);

    verify(cell1).moveShip(cell2);
    verifyZeroInteractions(modelBus);
  }

  @Test
  public void activate_group() {
    inactiveShips.add(ship1);
    inactiveShips.add(ship2);
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);
    when(ship2.inGroup(ShipGroup.PLAYER)).thenReturn(true);

    world.activateGroup(ShipGroup.PLAYER, Arrays.asList(cell1, cell2));

    assertThat(world.getInactiveShips()).isEmpty();
    verify(cell1).addShip(ship1);
    verify(cell2).addShip(ship2);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    SpawnShip spawnShip1 = TestHelpers.assertClass(argumentCaptor, 0, SpawnShip.class);
    assertThat(spawnShip1.getObject()).isSameAs(cell1);
    SpawnShip spawnShip2 = TestHelpers.assertClass(argumentCaptor, 1, SpawnShip.class);
    assertThat(spawnShip2.getObject()).isSameAs(cell2);
  }

  @Test
  public void deactivate_group() {
    cellMap.put(C0_1, cell1);
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell1.removeShip()).thenReturn(ship1);
    when(ship1.inGroup(ShipGroup.PLAYER)).thenReturn(true);

    world.deactivateGroup(ShipGroup.PLAYER);

    assertThat(world.getInactiveShips()).containsExactly(ship1);
    verify(cell1).removeShip();
    verify(modelBus).post(argumentCaptor.capture());
    RemoveShip removeShip = TestHelpers.assertClass(argumentCaptor.getValue(), RemoveShip.class);
    assertThat(removeShip.getObject()).isSameAs(ship1);
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
