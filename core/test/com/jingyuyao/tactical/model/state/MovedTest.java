package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovedTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);

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
  private SelectingTarget selectingTarget;
  @Mock
  private SelectingItem selectingItem;
  @Mock
  private Waiting waiting;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private Movement movement;
  @Mock
  private Moving moving;

  private Iterable<Weapon> weaponIterable;
  private Iterable<Consumable> consumableIterable;
  private Iterable<Item> itemIterable;
  private Moved moved;

  @Before
  public void setUp() {
    weaponIterable = ImmutableList.of(weapon);
    consumableIterable = ImmutableList.of(consumable);
    itemIterable = ImmutableList.of(weapon, consumable);
    moved = new Moved(mapState, stateFactory, movements, player);
  }

  @Test
  public void select_player() {
    moved.select(player);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_not_actionable() {
    when(otherPlayer.isActionable()).thenReturn(false);

    moved.select(otherPlayer);

    verify(mapState).rollback();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_other_player_actionable() {
    when(otherPlayer.isActionable()).thenReturn(true);
    when(movements.distanceFrom(otherPlayer)).thenReturn(movement);
    when(stateFactory.createMoving(otherPlayer, movement)).thenReturn(moving);

    moved.select(otherPlayer);

    verify(mapState).rollback();
    verify(mapState).goTo(moving);
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_enemy() {
    moved.select(enemy);

    verify(mapState).back();
    verifyNoMoreInteractions(mapState);
  }

  @Test
  public void select_terrain() {
    moved.select(terrain);

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
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(weapon.createTargets(COORDINATE)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);
    when(stateFactory.createSelectingItem(player)).thenReturn(selectingItem);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = moved.getActions();
    assertThat(actions).hasSize(5);
    return actions;
  }
}