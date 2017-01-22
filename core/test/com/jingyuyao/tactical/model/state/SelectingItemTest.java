package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectingItemTest {

  private final Coordinate COORDINATE = new Coordinate(0, 1);

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable1;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private SelectingTarget selectingTarget;
  @Mock
  private Waiting waiting;

  private Iterable<Weapon> weaponIterable;
  private Iterable<Consumable> consumableIterable;
  private SelectingItem selectingItem;

  @Before
  public void setUp() {
    weaponIterable = ImmutableList.of(weapon);
    consumableIterable = ImmutableList.of(consumable1);
    selectingItem = new SelectingItem(eventBus, mapState, stateFactory, player);
  }

  @Test
  public void select_player() {
    selectingItem.select(player);

    verify(mapState).pop();
  }

  @Test
  public void select_enemy() {
    selectingItem.select(enemy);

    verify(mapState).pop();
  }

  @Test
  public void select_terrain() {
    selectingItem.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void select_weapon() {
    ImmutableList<Action> actions = actions_set_up();

    Action selectWeapon = actions.get(0);
    selectWeapon.run();
    verify(player).quickAccess(weapon);
    verify(mapState).push(selectingTarget);
  }

  @Test
  public void actions_use_item() {
    ImmutableList<Action> actions = actions_set_up();

    Action useConsumable = actions.get(1);
    useConsumable.run();
    verify(consumable1).consume(player);
    verify(player).setActionable(false);
    verify(player).quickAccess(consumable1);
    verify(mapState).newStack(waiting);
  }

  @Test
  public void actions_back() {
    ImmutableList<Action> actions = actions_set_up();

    StateHelpers.verifyBack(actions.get(2), mapState);
  }

  private ImmutableList<Action> actions_set_up() {
    when(player.getWeapons()).thenReturn(weaponIterable);
    when(player.getConsumables()).thenReturn(consumableIterable);
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(weapon.createTargets(COORDINATE)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = selectingItem.getActions();
    assertThat(actions).hasSize(3);
    return actions;
  }
}