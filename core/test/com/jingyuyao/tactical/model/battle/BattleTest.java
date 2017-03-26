package com.jingyuyao.tactical.model.battle;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.Attack;
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
  private EventBus eventBus;
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
    battle = new Battle(eventBus);
  }

  @Test
  public void begin() {
    when(target.getTargetCells()).thenReturn(ImmutableList.of(cell1, cell2));
    when(cell1.hasCharacter()).thenReturn(true);
    when(cell1.getCharacter()).thenReturn(character1);
    when(character1.getHp()).thenReturn(0);
    when(cell2.hasCharacter()).thenReturn(true);
    when(cell2.getCharacter()).thenReturn(character2);
    when(character2.getHp()).thenReturn(10);

    ListenableFuture<Void> future = battle.begin(attacker, weapon, target);

    verify(eventBus).post(argumentCaptor.capture());
    Attack attack = TestHelpers.verifyObjectEvent(argumentCaptor, 0, target, Attack.class);
    assertThat(attack.getWeapon()).isSameAs(weapon);
    assertThat(future.isDone()).isFalse();
    verifyZeroInteractions(weapon);
    verifyZeroInteractions(attacker);

    attack.done();
    assertThat(future.isDone()).isTrue();
    verify(attacker).useItem(weapon);
    verify(weapon).damages(target);
    verify(cell1).removeCharacter();
    verify(cell2, never()).removeCharacter();
  }
}