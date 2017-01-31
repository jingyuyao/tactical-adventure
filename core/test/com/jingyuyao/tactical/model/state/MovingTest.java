package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovingTest {

  private static final Coordinate MOVING_PLAYER_COORDINATE = new Coordinate(0, 1);
  private static final Coordinate TERRAIN_COORDINATE = new Coordinate(0, 2);

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private Player player;
  @Mock
  private Player otherPlayer;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Moved moved;
  @Mock
  private SelectingTarget selectingTarget;
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
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private IgnoreInput ignoreInput;
  @Mock
  private Moving anotherMoving;
  @Mock
  private Movement otherMovement;

  private Iterable<Item> itemIterable;
  private Iterable<Weapon> weaponIterable;
  private Iterable<Consumable> consumableIterable;
  private ListenableFuture<Void> immediateFuture;
  private Moving moving;

  @Before
  public void setUp() {
    itemIterable = ImmutableList.<Item>of(weapon, consumable);
    weaponIterable = ImmutableList.of(weapon);
    consumableIterable = ImmutableList.of(consumable);
    // Futures are too hard to mock correctly
    immediateFuture = Futures.immediateFuture(null);
    moving = new Moving(mapState, stateFactory, movements, player, movement);
  }

  @Test
  public void enter() {
    moving.enter();

    verify(movement).showMarking();
  }

  @Test
  public void canceled_nothing() {
    moving.canceled();

    verifyZeroInteractions(player);
  }

  @Test
  public void canceled_terrain_move() {
    select_terrain_can_move();

    moving.canceled();
    moving.canceled();

    verify(player).instantMoveTo(MOVING_PLAYER_COORDINATE);
  }

  @Test
  public void exit() {
    enter();

    moving.exit();

    verify(movement).hideMarking();
  }

  @Test
  public void select_player() {
    moving.select(player);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_not_actionable() {
    when(otherPlayer.isActionable()).thenReturn(false);

    moving.select(otherPlayer);

    verify(mapState).rollback();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_actionable() {
    when(otherPlayer.isActionable()).thenReturn(true);
    when(movements.distanceFrom(otherPlayer)).thenReturn(otherMovement);
    when(stateFactory.createMoving(otherPlayer, otherMovement)).thenReturn(anotherMoving);

    moving.select(otherPlayer);

    verify(mapState).rollback();
    verify(mapState).goTo(anotherMoving);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_enemy() {
    moving.select(enemy);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_can_move() {
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(movement.canMoveTo(TERRAIN_COORDINATE)).thenReturn(true);
    when(movement.pathTo(TERRAIN_COORDINATE)).thenReturn(path);
    when(player.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(stateFactory.createMoved(player)).thenReturn(moved);
    when(stateFactory.createIgnoreInput()).thenReturn(ignoreInput);
    when(player.moveAlong(path)).thenReturn(immediateFuture);

    moving.select(terrain);

    InOrder inOrder = Mockito.inOrder(player, mapState);
    inOrder.verify(mapState).goTo(ignoreInput);
    inOrder.verify(player).moveAlong(path);
    inOrder.verify(mapState).goTo(moved);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain_cannot_move() {
    when(terrain.getCoordinate()).thenReturn(TERRAIN_COORDINATE);
    when(movement.canMoveTo(TERRAIN_COORDINATE)).thenReturn(false);

    moving.select(terrain);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_weapon() {
    ImmutableList<Action> actions = actions_set_up();

    Action selectWeapon = actions.get(0);
    selectWeapon.run();
    verify(player).quickAccess(weapon);
    verify(mapState).goTo(selectingTarget);
  }

  @Test
  public void use_consumable() {
    ImmutableList<Action> actions = actions_set_up();

    Action useConsumable = actions.get(1);
    useConsumable.run();
    verify(player).consumes(consumable);
    verify(player).setActionable(false);
    verify(player).quickAccess(consumable);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void select_items() {
    ImmutableList<Action> actions = actions_set_up();

    Action useItems = actions.get(2);
    useItems.run();
    verify(mapState).goTo(selectingItem);
  }

  @Test
  public void wait_action() {
    ImmutableList<Action> actions = actions_set_up();

    Action wait = actions.get(3);
    wait.run();
    verify(player).setActionable(false);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void back() {
    ImmutableList<Action> actions = actions_set_up();

    StateHelpers.verifyBack(actions.get(4), mapState);
  }

  private ImmutableList<Action> actions_set_up() {
    when(player.getItems()).thenReturn(itemIterable);
    when(player.getWeapons()).thenReturn(weaponIterable);
    when(player.getConsumables()).thenReturn(consumableIterable);
    when(player.getCoordinate()).thenReturn(MOVING_PLAYER_COORDINATE);
    when(weapon.createTargets(MOVING_PLAYER_COORDINATE)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);
    when(stateFactory.createSelectingItem(player)).thenReturn(selectingItem);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = moving.getActions();
    assertThat(actions).hasSize(5);
    return actions;
  }
}
