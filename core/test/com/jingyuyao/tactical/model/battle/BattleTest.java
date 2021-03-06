package com.jingyuyao.tactical.model.battle;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BattleTest {

  @Mock
  private Cell attackerCell;
  @Mock
  private Ship attacker;
  @Mock
  private Weapon weapon;
  @Mock
  private Target target;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;

  private Battle battle;

  @Before
  public void setUp() {
    battle = new Battle(attackerCell, weapon, target);
    assertThat(battle.getWeapon()).isSameAs(weapon);
    assertThat(battle.getTarget()).isSameAs(target);
  }

  @Test
  public void execute_attacker_alive() {
    when(attackerCell.ship()).thenReturn(Optional.of(attacker));
    when(attacker.getHp()).thenReturn(10);
    when(target.getTargetCells()).thenReturn(new HashSet<>(Arrays.asList(cell1, cell2)));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(ship1.getHp()).thenReturn(0);
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship2.getHp()).thenReturn(10);

    battle.execute();

    verify(weapon).apply(attacker, target);
    verify(attacker).useWeapon(weapon);
    verify(ship1).useEquippedArmors();
    verify(ship2).useEquippedArmors();
    assertThat(battle.getDeadCells()).containsExactly(cell1);
  }

  @Test
  public void execute_attacker_dead() {
    when(attackerCell.ship()).thenReturn(Optional.of(attacker));
    when(attacker.getHp()).thenReturn(0);
    when(target.getTargetCells()).thenReturn(new HashSet<>(Arrays.asList(cell1, cell2)));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(ship1.getHp()).thenReturn(0);
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship2.getHp()).thenReturn(10);

    battle.execute();

    verify(weapon).apply(attacker, target);
    verify(attacker).useWeapon(weapon);
    verify(ship1).useEquippedArmors();
    verify(ship2).useEquippedArmors();
    assertThat(battle.getDeadCells()).containsExactly(cell1, attackerCell);
  }

  @Test
  public void execute_attacker_part_of_target_dead() {
    when(attackerCell.ship()).thenReturn(Optional.of(attacker));
    when(attacker.getHp()).thenReturn(0);
    when(target.getTargetCells())
        .thenReturn(new HashSet<>(Collections.singletonList(attackerCell)));

    battle.execute();

    verify(weapon).apply(attacker, target);
    verify(attacker).useWeapon(weapon);
    verify(attacker).useEquippedArmors();
    assertThat(battle.getDeadCells()).containsExactly(attackerCell);
  }
}