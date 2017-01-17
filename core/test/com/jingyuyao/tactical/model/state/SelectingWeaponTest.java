package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectingWeaponTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Enemy enemy;
  @Mock
  private Player player;
  @Mock
  private Terrain terrain;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private ImmutableList<Target> targets1;
  @Mock
  private ImmutableList<Target> targets2;
  @Mock
  private SelectingTarget selectingTarget1;
  @Mock
  private SelectingTarget selectingTarget2;

  private Iterable<Weapon> weapons;
  private SelectingWeapon selectingWeapon;

  @Before
  public void setUp() {
    weapons = ImmutableList.of(weapon1, weapon2);
    selectingWeapon = new SelectingWeapon(eventBus, mapState, stateFactory, player, weapons);
  }

  @Test
  public void select_player() {
    selectingWeapon.select(player);

    verify(mapState).pop();
  }

  @Test
  public void select_enemy() {
    selectingWeapon.select(enemy);

    verify(mapState).pop();
  }

  @Test
  public void select_terrain() {
    selectingWeapon.select(terrain);

    verify(mapState).pop();
  }

  @Test
  public void actions_select_weapon() {
    ImmutableList<Action> actions = actionsSetUp();

    Action selectWeapon1 = actions.get(0);
    selectWeapon1.run();
    verify(mapState).push(selectingTarget1);

    Action selectWeapon2 = actions.get(1);
    selectWeapon2.run();
    verify(mapState).push(selectingTarget2);
  }

  @Test
  public void actions_back() {
    ImmutableList<Action> actions = actionsSetUp();

    StateHelpers.verifyBack(actions.get(2), mapState);
  }

  private ImmutableList<Action> actionsSetUp() {
    when(weapon1.createTargets(player)).thenReturn(targets1);
    when(weapon2.createTargets(player)).thenReturn(targets2);
    when(stateFactory.createSelectingTarget(player, targets1)).thenReturn(selectingTarget1);
    when(stateFactory.createSelectingTarget(player, targets2)).thenReturn(selectingTarget2);
    ImmutableList<Action> actions = selectingWeapon.getActions();
    assertThat(actions).hasSize(3);
    return actions;
  }
}