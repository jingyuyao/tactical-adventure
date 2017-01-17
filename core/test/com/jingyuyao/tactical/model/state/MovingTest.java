package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovingTest {

  private static final Coordinate MOVING_PLAYER_COORDINATE = new Coordinate(0, 1);
  private static final Coordinate ENEMY_COORDINATE = new Coordinate(0, 0);
  private static final Coordinate TERRAIN_COORDINATE = new Coordinate(0, 2);

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private MovementFactory movementFactory;
  @Mock
  private Player movingPlayer;
  @Mock
  private Player anotherPlayer;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Moved moved;
  @Mock
  private Moving dummyMoving;
  @Mock
  private SelectingWeapon selectingWeapon;
  @Mock
  private SelectingItem selectingItem;
  @Mock
  private Waiting waiting;
  @Mock
  private Movement movement;
  @Mock
  private Path path;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;

  private Iterable<Weapon> weaponIterable;
  private Iterable<Consumable> consumableIterable;
  private Iterable<Terrain> terrainList;
  private ListenableFuture<Void> immediateFuture;
  private Moving moving;

  @Before
  public void setUp() {
    weaponIterable = ImmutableList.of(weapon);
    consumableIterable = ImmutableList.of(consumable);
    terrainList = ImmutableList.of(terrain);
    // Futures are too hard to mock correctly
    immediateFuture = Futures.immediateFuture(null);
    moving = new Moving(eventBus, mapState, stateFactory, movingPlayer, movementFactory);
  }

  @Test
  public void enter() {
    when(movementFactory.create(movingPlayer)).thenReturn(movement);
    when(movement.getTerrains()).thenReturn(terrainList);

    moving.enter();

    verify(terrain).addMarker(Marker.CAN_MOVE_TO);
  }

  @Test
  public void canceled_nothing() {
    moving.canceled();

    verifyZeroInteractions(movingPlayer);
  }

  @Test
  public void canceled_terrain_move() {
    select_terrain_can_move();

    moving.canceled();

    verify(movementFactory).create(movingPlayer);
    verify(movingPlayer).getCoordinate();
    verify(movingPlayer).instantMoveTo(MOVING_PLAYER_COORDINATE);

    moving.canceled();

    verifyNoMoreInteractions(movingPlayer);
  }

  @Test
  public void exit() {
    enter();

    moving.exit();

    verify(terrain).removeMarker(Marker.CAN_MOVE_TO);
  }

  @Test
  public void select_differentPlayer() {
    when(stateFactory.createMoving(anotherPlayer)).thenReturn(dummyMoving);

    moving.select(anotherPlayer);

    verify(stateFactory).createMoving(anotherPlayer);
    verify(mapState).rollback();
    verify(mapState).push(dummyMoving);
  }

  @Test
  public void select_enemy() {
    moving.select(enemy);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_can_move() {
    when(movementFactory.create(movingPlayer)).thenReturn(movement);
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(movement.canMoveTo(TERRAIN_COORDINATE)).thenReturn(true);
    when(movement.pathTo(TERRAIN_COORDINATE)).thenReturn(path);
    when(movingPlayer.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(stateFactory.createMoved(movingPlayer)).thenReturn(moved);
    when(movingPlayer.move(path)).thenReturn(immediateFuture);

    moving.select(terrain);

    verify(movingPlayer).getCoordinate();
    verify(movementFactory).create(movingPlayer);
    verify(movingPlayer).move(path);
    verify(mapState).push(moved);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_cannot_move() {
    when(movementFactory.create(movingPlayer)).thenReturn(movement);
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(movement.canMoveTo(TERRAIN_COORDINATE)).thenReturn(false);

    moving.select(terrain);

    verify(mapState).pop();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = actions_set_up();

    Action selectWeapons = actions.get(0);
    selectWeapons.run();
    verify(mapState).push(selectingWeapon);

    Action useItems = actions.get(1);
    useItems.run();
    verify(mapState).push(selectingItem);

    Action wait = actions.get(2);
    wait.run();
    verify(movingPlayer).setActionable(false);
    verify(mapState).newStack(waiting);

    StateHelpers.verifyBack(actions.get(3), mapState);
  }

  private ImmutableList<Action> actions_set_up() {
    when(movingPlayer.getWeapons()).thenReturn(weaponIterable);
    when(movingPlayer.getConsumables()).thenReturn(consumableIterable);
    when(stateFactory.createSelectingWeapon(movingPlayer)).thenReturn(selectingWeapon);
    when(stateFactory.createSelectingItem(movingPlayer)).thenReturn(selectingItem);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = moving.getActions();
    assertThat(actions).hasSize(4);
    return actions;
  }
}
