package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.ship.Ship;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CellTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 3);

  @Mock
  private Terrain terrain;
  @Mock
  private Ship ship;

  private Cell cell;

  @Before
  public void setUp() {
    cell = new Cell(COORDINATE, terrain);
    assertThat(cell.getCoordinate()).isEqualTo(COORDINATE);
    assertThat(cell.getTerrain()).isSameAs(terrain);
    assertThat(cell.ship()).isAbsent();
  }

  @Test
  public void add_ship() {
    when(terrain.canHoldShip()).thenReturn(true);

    cell.addShip(ship);

    assertThat(cell.ship()).hasValue(ship);
  }

  @Test(expected = IllegalStateException.class)
  public void add_ship_multi() {
    when(terrain.canHoldShip()).thenReturn(true);

    cell.addShip(ship);
    cell.addShip(ship);
  }

  @Test(expected = IllegalArgumentException.class)
  public void add_ship_cannot_hold() {
    when(terrain.canHoldShip()).thenReturn(false);

    cell.addShip(ship);
  }

  @Test
  public void remove_ship() {
    when(terrain.canHoldShip()).thenReturn(true);
    cell.addShip(ship);

    cell.removeShip();

    assertThat(cell.ship()).isAbsent();
  }

  @Test(expected = IllegalStateException.class)
  public void remove_ship_exception() {
    when(terrain.canHoldShip()).thenReturn(true);
    cell.addShip(ship);

    cell.removeShip();
    cell.removeShip();
  }

  @Test
  public void move_ship_same_cell() {
    when(terrain.canHoldShip()).thenReturn(true);
    cell.addShip(ship);

    assertThat(cell.moveShip(cell)).isAbsent();
    assertThat(cell.ship()).hasValue(ship);
  }

  @Test
  public void move_ship() {
    when(terrain.canHoldShip()).thenReturn(true);
    Cell other = new Cell(COORDINATE, terrain);
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
    when(terrain.canHoldShip()).thenReturn(true);
    Cell other = new Cell(COORDINATE, terrain);
    cell.addShip(ship);
    other.addShip(ship);

    cell.moveShip(other);
  }
}