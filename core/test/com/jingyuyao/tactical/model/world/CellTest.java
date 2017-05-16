package com.jingyuyao.tactical.model.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
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
  private Player player;
  @Mock
  private Enemy enemy;
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
    assertThat(cell.player()).isAbsent();
    assertThat(cell.enemy()).isAbsent();
  }

  @Test
  public void spawn_player() {
    cell.spawnShip(player);

    assertThat(cell.ship()).hasValue(player);
    assertThat(cell.player()).hasValue(player);
    assertThat(cell.enemy()).isAbsent();
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
  }

  @Test
  public void spawn_enemy() {
    cell.spawnShip(enemy);

    assertThat(cell.ship()).hasValue(enemy);
    assertThat(cell.enemy()).hasValue(enemy);
    assertThat(cell.player()).isAbsent();
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
  }

  @Test
  public void remove_ship() {
    cell.spawnShip(player);
    cell.removeShip();

    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, player, RemoveShip.class);
  }

  @Test
  public void instant_move_ship() {
    Cell other = new Cell(modelBus, COORDINATE, terrain);
    cell.spawnShip(player);

    cell.instantMoveShip(other);

    assertThat(cell.ship()).isAbsent();
    assertThat(other.ship()).hasValue(player);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(InstantMoveShip.class);
  }

  @Test
  public void instant_move_same_cell() {
    cell.spawnShip(player);

    cell.instantMoveShip(cell);

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(cell.ship()).hasValue(player);
    assertThat(cell.player()).hasValue(player);
  }

  @Test
  public void move_ship() {
    Cell other = new Cell(modelBus, COORDINATE, terrain);
    cell.spawnShip(player);
    when(path.getOrigin()).thenReturn(cell);
    when(path.getDestination()).thenReturn(other);

    Promise promise = cell.moveShip(path);

    assertThat(promise.isDone()).isFalse();
    assertThat(cell.ship()).isAbsent();
    assertThat(other.ship()).hasValue(player);
    verify(modelBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(argumentCaptor.getAllValues().get(1)).isInstanceOf(MoveShip.class);
  }

  @Test
  public void move_ship_same_cell() {
    cell.spawnShip(player);
    when(path.getOrigin()).thenReturn(cell);
    when(path.getDestination()).thenReturn(cell);

    Promise promise = cell.moveShip(path);

    assertThat(promise.isDone()).isTrue();
    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SpawnShip.class);
    assertThat(cell.ship()).hasValue(player);
    assertThat(cell.player()).hasValue(player);
  }
}