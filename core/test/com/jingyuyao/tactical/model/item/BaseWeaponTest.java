package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.verify;
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
  private Character character1;
  @Mock
  private Character character2;

  private BaseWeapon baseWeapon;

  @Before
  public void setUp() {
    baseWeapon = new BaseWeapon(10);
  }

  @Test
  public void damages() {
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell1, cell2));
    when(cell1.character()).thenReturn(Optional.of(character1));
    when(cell2.character()).thenReturn(Optional.of(character2));
    when(character1.getDefense()).thenReturn(3);
    when(character2.getDefense()).thenReturn(100);

    baseWeapon.damages(target);

    verify(character1).damageBy(7);
    verify(character2).damageBy(0);
  }
}