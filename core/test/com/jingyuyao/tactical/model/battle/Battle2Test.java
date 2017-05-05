package com.jingyuyao.tactical.model.battle;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Battle2Test {

  @Mock
  private Cell attackerCell;
  @Mock
  private Character attacker;
  @Mock
  private Weapon weapon;
  @Mock
  private Target target;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Character character1;
  @Mock
  private Character character2;

  private Battle2 battle2;

  @Before
  public void setUp() {
    battle2 = new Battle2(attackerCell, weapon, target, new MyFuture());
    assertThat(battle2.getWeapon()).isSameAs(weapon);
    assertThat(battle2.getTarget()).isSameAs(target);
    assertThat(battle2.getFuture().isDone()).isFalse();
  }

  @Test
  public void execute_attacker_alive() {
    when(attackerCell.character()).thenReturn(Optional.of(attacker));
    when(attacker.getHp()).thenReturn(10);
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell1, cell2));
    when(cell1.character()).thenReturn(Optional.of(character1));
    when(character1.getHp()).thenReturn(0);
    when(cell2.character()).thenReturn(Optional.of(character2));
    when(character2.getHp()).thenReturn(10);

    battle2.execute();

    verify(weapon).apply(attacker, target);
    verify(attacker).useWeapon(weapon);
    verify(attackerCell, never()).removeCharacter();
    verify(character1).useEquippedArmors();
    verify(character2).useEquippedArmors();
    verify(cell1).removeCharacter();
    verify(cell2, never()).removeCharacter();
    assertThat(battle2.getFuture().isDone()).isTrue();
  }

  @Test
  public void execute_attacker_dead() {
    when(attackerCell.character()).thenReturn(Optional.of(attacker));
    when(attacker.getHp()).thenReturn(0);
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell1, cell2));
    when(cell1.character()).thenReturn(Optional.of(character1));
    when(character1.getHp()).thenReturn(0);
    when(cell2.character()).thenReturn(Optional.of(character2));
    when(character2.getHp()).thenReturn(10);

    battle2.execute();

    verify(weapon).apply(attacker, target);
    verify(attacker).useWeapon(weapon);
    verify(attackerCell).removeCharacter();
    verify(character1).useEquippedArmors();
    verify(character2).useEquippedArmors();
    verify(cell1).removeCharacter();
    verify(cell2, never()).removeCharacter();
    assertThat(battle2.getFuture().isDone()).isTrue();
  }
}