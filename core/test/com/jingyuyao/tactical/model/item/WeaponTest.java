package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeaponTest {

  private static final String NAME = "ok";
  private static final int INITIAL_USAGE = 2;
  private static final int ATTACK_POWER = 5;

  @Mock
  private EventBus eventBus;
  @Mock
  private Set<Integer> attackDistances;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Weapon weapon;

  @Before
  public void setUp() {
    weapon = new Weapon(eventBus, NAME, INITIAL_USAGE, ATTACK_POWER, attackDistances);
  }

  @Test
  public void name() {
    assertThat(weapon.getName()).isEqualTo(NAME);
  }

  @Test
  public void get_attack_distances() {
    assertThat(weapon.getAttackDistances()).isSameAs(attackDistances);
  }

  @Test
  public void get_attack_power() {
    assertThat(weapon.getAttackPower()).isEqualTo(ATTACK_POWER);
  }

  @Test
  public void use() {
    weapon.useOnce();

    verifyZeroInteractions(eventBus);

    weapon.useOnce();
    verify(eventBus).post(argumentCaptor.capture());
    RemoveItem removeItem = TestHelpers.isInstanceOf(argumentCaptor.getValue(), RemoveItem.class);
    assertThat(removeItem.getObject()).isSameAs(weapon);
  }
}