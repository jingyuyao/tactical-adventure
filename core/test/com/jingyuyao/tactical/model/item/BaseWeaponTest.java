package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseWeaponTest {

  @Mock
  private Target target;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Character attacker;
  @Mock
  private Character character1;
  @Mock
  private Character character2;

  private BaseWeapon baseWeapon;

  @Before
  public void setUp() {
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell1, cell2));
    when(cell1.character()).thenReturn(Optional.of(character1));
    when(cell2.character()).thenReturn(Optional.of(character2));
    when(character1.getDefense()).thenReturn(3);
    when(character2.getDefense()).thenReturn(100);
  }

  @Test
  public void apply_basic() {
    baseWeapon = new BaseWeapon(10, false, 0, false, 0);
    baseWeapon.apply(attacker, target);

    verify(character1).damageBy(7);
    verify(character2).damageBy(0);
    verifyZeroInteractions(attacker);
  }

  @Test
  public void apply_life_steal() {
    baseWeapon = new BaseWeapon(10, true, 0.5f, false, 0);
    baseWeapon.apply(attacker, target);

    verify(character1).damageBy(7);
    verify(character2).damageBy(0);
    verify(attacker).healBy(3);
  }

  @Test
  public void apply_recoil() {
    baseWeapon = new BaseWeapon(10, false, 0, true, 0.5f);
    baseWeapon.apply(attacker, target);

    verify(character1).damageBy(7);
    verify(character2).damageBy(0);
    verify(attacker).damageBy(3);
  }
}