package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CellTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 3);

  @Mock
  private ModelBus modelBus;
  @Mock
  private Terrain terrain;
  @Mock
  private Ship ship;
  @Mock
  private Path path;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Cell cell;

  @Before
  public void setUp() {
    cell = new Cell(modelBus, COORDINATE, terrain);
    assertThat(cell.getCoordinate()).isEqualTo(COORDINATE);
    assertThat(cell.getTerrain()).isSameAs(terrain);
    assertThat(cell.ship()).isAbsent();
  }

  @Test
  public void add_ship() {
    when(terrain.canHold(ship)).thenReturn(true);

    cell.addShip(ship);

    assertThat(cell.ship()).hasValue(ship);
  }

  @Test(expected = IllegalStateException.class)
  public void add_ship_multi() {
    when(terrain.canHold(ship)).thenReturn(true);

    cell.addShip(ship);
    cell.addShip(ship);
  }

  @Test(expected = IllegalArgumentException.class)
  public void add_ship_cannot_hold() {
    when(terrain.canHold(ship)).thenReturn(false);

    cell.addShip(ship);
  }

  @Test
  public void del_ship() {
    when(terrain.canHold(ship)).thenReturn(true);
    cell.addShip(ship);

    cell.delShip();

    assertThat(cell.ship()).isAbsent();
  }

  @Test(expected = IllegalStateException.class)
  public void del_ship_exception() {
    when(terrain.canHold(ship)).thenReturn(true);
    cell.addShip(ship);

    cell.delShip();
    cell.delShip();
  }

  @Test
  public void move_ship_same_cell() {
    when(terrain.canHold(ship)).thenReturn(true);
    cell.addShip(ship);

    assertThat(cell.moveShip(cell)).isAbsent();
    assertThat(cell.ship()).hasValue(ship);
  }

  @Test
  public void move_ship() {
    when(terrain.canHold(ship)).thenReturn(true);
    Cell other = new Cell(modelBus, COORDINATE, terrain);
    cell.addShip(ship);

    cell.moveShip(other);

    assertThat(cell.ship()).isAbsent();
    assertThat(other.ship()).hasValue(ship);
  }

  @Test(expected = IllegalStateException.class)
  public void move_ship_no_ship() {
    cell.moveShip(cell);
  }

  @Test(expected = IllegalStateException.class)
  public void move_ship_other_has_ship() {
    when(terrain.canHold(ship)).thenReturn(true);
    Cell other = new Cell(modelBus, COORDINATE, terrain);
    cell.addShip(ship);
    other.addShip(ship);

    cell.moveShip(other);
  }

  @Test
  public void spawn_ship() {
    cell.spawnShip(ship);

    assertThat(cell.ship()).hasValue(ship);
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
  }

  @Test
  public void remove_ship() {
    cell.spawnShip(ship);
    cell.removeShip();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, ship, RemoveShip.class);
  }

  @Test
  public void instant_move_ship() {
    Cell other = new Cell(modelBus, COORDINATE, terrain);
    cell.spawnShip(ship);

    cell.instantMoveShip(other);

    assertThat(cell.ship()).isAbsent();
    assertThat(other.ship()).hasValue(ship);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(InstantMoveShip.class);
  }

  @Test
  public void instant_move_same_cell() {
    cell.spawnShip(ship);

    cell.instantMoveShip(cell);

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(cell.ship()).hasValue(ship);
  }

  @Test
  public void move_ship_old() {
    Cell other = new Cell(modelBus, COORDINATE, terrain);
    cell.spawnShip(ship);
    when(path.getOrigin()).thenReturn(cell);
    when(path.getDestination()).thenReturn(other);

    Promise promise = cell.moveShip(path);

    assertThat(promise.isDone()).isFalse();
    assertThat(cell.ship()).isAbsent();
    assertThat(other.ship()).hasValue(ship);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(MoveShip.class);
  }

  @Test
  public void move_ship_same_cell_old() {
    cell.spawnShip(ship);
    when(path.getOrigin()).thenReturn(cell);
    when(path.getDestination()).thenReturn(cell);

    Promise promise = cell.moveShip(path);

    assertThat(promise.isDone()).isTrue();
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(cell.ship()).hasValue(ship);
  }
}