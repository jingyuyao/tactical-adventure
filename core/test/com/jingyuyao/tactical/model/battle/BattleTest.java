package com.jingyuyao.tactical.model.battle;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.Attack;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
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
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Battle battle;

  @Before
  public void setUp() {
    battle = new Battle(eventBus);
  }

  @Test
  public void begin() {
    ListenableFuture<Void> future = battle.begin(attacker, weapon, target);

    verify(eventBus).post(argumentCaptor.capture());
    Attack attack = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Attack.class);
    assertThat(attack.getObject()).isSameAs(target);
    assertThat(future.isDone()).isFalse();
    verifyZeroInteractions(weapon);
    verifyZeroInteractions(attacker);

    attack.done();
    assertThat(future.isDone()).isTrue();
    verify(weapon).damages(target);
    verify(attacker).useItem(weapon);
  }
}