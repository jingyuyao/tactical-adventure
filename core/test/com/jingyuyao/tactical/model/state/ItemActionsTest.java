package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemActionsTest {

  private static final Coordinate PLAYER_COORDINATE = new Coordinate(101, 101);

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Weapon weapon;
  @Mock
  private Consumable consumable;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private SelectingTarget selectingTarget;
  @Mock
  private Waiting waiting;

  private ItemActions itemActions;

  @Before
  public void setUp() {
    itemActions = new ItemActions(mapState, stateFactory, player);
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
    verify(player).useItem(consumable);
    verify(consumable).apply(player);
    verify(player).setActionable(false);
    verify(player).quickAccess(consumable);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void wait_action() {
    ImmutableList<Action> actions = actions_set_up();

    Action wait = actions.get(2);
    wait.run();
    verify(player).setActionable(false);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void back() {
    ImmutableList<Action> actions = actions_set_up();

    StateHelpers.verifyBack(actions.get(3), mapState);
  }

  private ImmutableList<Action> actions_set_up() {
    when(player.fluentItems()).thenReturn(FluentIterable.of(weapon, consumable));
    when(player.getCoordinate()).thenReturn(PLAYER_COORDINATE);
    when(weapon.createTargets(PLAYER_COORDINATE)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    ImmutableList<Action> actions = itemActions.getActions();
    assertThat(actions).hasSize(4);
    return actions;
  }
}