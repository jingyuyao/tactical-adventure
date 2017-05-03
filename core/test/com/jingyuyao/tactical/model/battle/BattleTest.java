package com.jingyuyao.tactical.model.battle;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BattleTest {

  @Mock
  private ModelBus modelBus;
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
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Battle battle;

  @Before
  public void setUp() {
    battle = new Battle(modelBus);
  }

  @Test
  public void begin() {
    when(target.getTargetCells()).thenReturn(ImmutableSet.of(cell1, cell2));
    when(cell1.character()).thenReturn(Optional.of(character1));
    when(character1.getHp()).thenReturn(0);
    when(cell2.character()).thenReturn(Optional.of(character2));
    when(character2.getHp()).thenReturn(10);

    MyFuture future = battle.begin(attacker, weapon, target);

    verify(modelBus).post(argumentCaptor.capture());
    Attack attack = TestHelpers.verifyObjectEvent(argumentCaptor, 0, target, Attack.class);
    assertThat(attack.getWeapon()).isSameAs(weapon);
    assertThat(attack.getFuture()).isSameAs(future);
    assertThat(future.isDone()).isFalse();
    verifyZeroInteractions(weapon);
    verifyZeroInteractions(attacker);

    future.done();
    assertThat(future.isDone()).isTrue();
    verify(attacker).useWeapon(weapon);
    verify(weapon).damages(target);
    verify(character1).useEquippedArmors();
    verify(character2).useEquippedArmors();
    verify(cell1).removeCharacter();
    verify(cell2, never()).removeCharacter();
  }
}