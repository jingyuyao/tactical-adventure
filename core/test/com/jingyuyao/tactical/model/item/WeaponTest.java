package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeaponTest {

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

  private Weapon weapon;

  @Before
  public void setUp() {
    when(target.getTargetCells()).thenReturn(new HashSet<Cell>(Arrays.asList(cell1, cell2)));
    when(cell1.ship()).thenReturn(Optional.of(ship1));
    when(cell2.ship()).thenReturn(Optional.of(ship2));
    when(ship1.getDefense()).thenReturn(3);
    when(ship2.getDefense()).thenReturn(100);
  }

  @Test
  public void apply_basic() {
    weapon = new Weapon("name", 5, 10, 0, 0);
    weapon.apply(attacker, target);

    verify(ship1).damageBy(7);
    verify(ship2).damageBy(0);
    verify(attacker, times(2)).damageBy(0);
    verify(attacker, times(2)).healBy(0);
  }

  @Test
  public void apply_life_steal() {
    weapon = new Weapon("name", 5, 10, 0.5f, 0);
    weapon.apply(attacker, target);

    verify(ship1).damageBy(7);
    verify(ship2).damageBy(0);
    verify(attacker, times(2)).damageBy(0);
    verify(attacker).healBy(3);
    verify(attacker).healBy(0);
  }

  @Test
  public void apply_recoil() {
    weapon = new Weapon("name", 5, 10, 0, 0.5f);
    weapon.apply(attacker, target);

    verify(ship1).damageBy(7);
    verify(ship2).damageBy(0);
    verify(attacker, times(2)).damageBy(5);
    verify(attacker, times(2)).healBy(0);
  }
}