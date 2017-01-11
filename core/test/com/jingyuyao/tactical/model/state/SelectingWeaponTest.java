package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Terrain;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectingWeaponTest {

  private static final Coordinate PLAYER_COORDINATE = new Coordinate(0, 0);
  private static final Coordinate ENEMY_COORDINATE = new Coordinate(0, 1);

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
  private AttackPlanFactory attackPlanFactory;
  @Mock
  private Terrain terrain;
  @Mock
  private Iterable<Weapon> weapons;
  @Mock
  private Iterator<Weapon> weaponIterator;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private Targets targets;
  @Mock
  private ImmutableSet<Weapon> targetingWeapons;
  @Mock
  private AttackPlan attackPlan;
  @Mock
  private ReviewingAttack reviewingAttack;

  private SelectingWeapon selectingWeapon;

  @Before
  public void setUp() {
    selectingWeapon =
        new SelectingWeapon(eventBus, mapState, stateFactory, player, enemy, attackPlanFactory);
  }

  @Test
  public void enter() {
    selectingWeapon.enter();

    verify(player).showImmediateTargets();
  }

  @Test
  public void exit() {
    selectingWeapon.exit();

    verify(player).clearMarking();
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

    Action selectWeapon = actions.get(0);
    selectWeapon.run();
    verify(mapState).push(reviewingAttack);
  }

  @Test
  public void actions_back() {
    ImmutableList<Action> actions = actionsSetUp();

    StateHelpers.verifyBack(actions.get(1), mapState);
  }

  private ImmutableList<Action> actionsSetUp() {
    when(player.createTargets()).thenReturn(targets);
    when(player.getCoordinate()).thenReturn(PLAYER_COORDINATE);
    when(enemy.getCoordinate()).thenReturn(ENEMY_COORDINATE);
    when(targets.availableWeapons(PLAYER_COORDINATE, ENEMY_COORDINATE))
        .thenReturn(targetingWeapons);
    when(targetingWeapons.contains(weapon2)).thenReturn(true);
    when(player.getWeapons()).thenReturn(weapons);
    when(weapons.iterator()).thenReturn(weaponIterator);
    when(weaponIterator.hasNext()).thenReturn(true, true, false);
    when(weaponIterator.next()).thenReturn(weapon1, weapon2);
    when(attackPlanFactory.create(player, enemy)).thenReturn(attackPlan);
    when(stateFactory.createReviewingAttack(player, attackPlan)).thenReturn(reviewingAttack);
    ImmutableList<Action> actions = selectingWeapon.getActions();
    assertThat(actions).hasSize(2);
    return actions;
  }
}