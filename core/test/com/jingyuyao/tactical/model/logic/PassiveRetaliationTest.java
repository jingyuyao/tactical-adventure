package com.jingyuyao.tactical.model.logic;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.Targets.FilteredTargets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PassiveRetaliationTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(0, 1);

  @Mock
  private AttackPlanFactory attackPlanFactory;
  @Mock
  private Targets targets;
  @Mock
  private FilteredTargets allTargets;
  @Mock
  private Enemy enemy;
  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Path path;
  @Mock
  private Weapon weapon;
  @Mock
  private AttackPlan attackPlan;

  private Iterable<Character> characterIterable;
  private Iterable<Weapon> weaponIterable;
  private ListenableFuture<Void> immediateFuture;
  private PassiveRetaliation passiveRetaliation;

  @Before
  public void setUp() {
    characterIterable = ImmutableList.of(enemy, player1, player2);
    weaponIterable = ImmutableList.of(weapon);
    immediateFuture = Futures.immediateFuture(null);
    passiveRetaliation = new PassiveRetaliation(attackPlanFactory);
  }

  @Test
  public void run() {
    when(enemy.createTargets()).thenReturn(targets);
    when(targets.all()).thenReturn(allTargets);
    when(allTargets.characters()).thenReturn(characterIterable);
    when(player1.getHp()).thenReturn(10);
    when(player2.getHp()).thenReturn(5);
    when(player2.getCoordinate()).thenReturn(COORDINATE);
    when(targets.movePathToTargetCoordinate(COORDINATE)).thenReturn(path);
    when(enemy.getWeapons()).thenReturn(weaponIterable);
    when(path.getDestination()).thenReturn(COORDINATE2);
    when(weapon.canHitFrom(enemy, COORDINATE2, player2)).thenReturn(true);
    when(enemy.move(path)).thenReturn(immediateFuture);
    when(attackPlanFactory.create(enemy, player2)).thenReturn(attackPlan);

    ListenableFuture<Void> future = passiveRetaliation.run(enemy);

    verify(enemy).equipWeapon(weapon);
    verify(attackPlanFactory).create(enemy, player2);
    verify(attackPlan).execute();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void run_no_target() {
    when(enemy.createTargets()).thenReturn(targets);
    when(targets.all()).thenReturn(allTargets);
    when(allTargets.characters()).thenReturn(ImmutableList.<Character>of());

    ListenableFuture<Void> future = passiveRetaliation.run(enemy);

    assertThat(future.isDone()).isTrue();
  }
}