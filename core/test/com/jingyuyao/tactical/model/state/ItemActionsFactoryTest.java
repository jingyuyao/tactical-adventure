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
public class ItemActionsFactoryTest {

  private static final Coordinate PLAYER_COORDINATE = new Coordinate(101, 101);

  @Mock
  private AbstractPlayerState playerState;
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

  private ItemActionsFactory itemActionsFactory;

  @Before
  public void setUp() {
    itemActionsFactory = new ItemActionsFactory();
  }

  @Test
  public void select_weapon() {
    ImmutableList<Action> actions = actions_set_up();

    Action selectWeapon = actions.get(0);
    selectWeapon.run();
    verify(player).quickAccess(weapon);
    verify(playerState).goTo(selectingTarget);
  }

  @Test
  public void use_consumable() {
    ImmutableList<Action> actions = actions_set_up();

    Action useConsumable = actions.get(1);
    useConsumable.run();
    verify(player).useItem(consumable);
    verify(consumable).apply(player);
    verify(player).quickAccess(consumable);
    verify(playerState).finish();
  }

  private ImmutableList<Action> actions_set_up() {
    when(playerState.getPlayer()).thenReturn(player);
    when(playerState.getStateFactory()).thenReturn(stateFactory);
    when(player.fluentItems()).thenReturn(FluentIterable.of(weapon, consumable));
    when(player.getCoordinate()).thenReturn(PLAYER_COORDINATE);
    when(weapon.createTargets(PLAYER_COORDINATE)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);
    ImmutableList<Action> actions = itemActionsFactory.create(playerState);
    assertThat(actions).hasSize(2);
    return actions;
  }
}