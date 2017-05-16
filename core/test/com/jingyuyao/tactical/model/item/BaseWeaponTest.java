package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Ship;
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
  private Ship attacker;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;

  private BaseWeapon baseWeapon;

  @Before
  public void setUp() {
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell1, cell2));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.getDefense()).thenReturn(3);
    when(ship2.getDefense()).thenReturn(100);
  }

  @Test
  public void apply_basic() {
    baseWeapon = new BaseWeapon(10, 0, 0);
    baseWeapon.apply(attacker, target);

    verify(ship1).damageBy(7);
    verify(ship2).damageBy(0);
    verify(attacker, times(2)).damageBy(0);
    verify(attacker, times(2)).healBy(0);
  }

  @Test
  public void apply_life_steal() {
    baseWeapon = new BaseWeapon(10, 0.5f, 0);
    baseWeapon.apply(attacker, target);

    verify(ship1).damageBy(7);
    verify(ship2).damageBy(0);
    verify(attacker, times(2)).damageBy(0);
    verify(attacker).healBy(3);
    verify(attacker).healBy(0);
  }

  @Test
  public void apply_recoil() {
    baseWeapon = new BaseWeapon(10, 0, 0.5f);
    baseWeapon.apply(attacker, target);

    verify(ship1).damageBy(7);
    verify(ship2).damageBy(0);
    verify(attacker).damageBy(3);
    verify(attacker).damageBy(0);
    verify(attacker, times(2)).healBy(0);
  }
}