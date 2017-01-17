package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.battle.PiercingFactory;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.common.Directions;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
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
  private static final Coordinate COORDINATE = new Coordinate(10, 10);

  @Mock
  private EventBus eventBus;
  @Mock
  private Enemy enemy;
  @Mock
  private PiercingFactory piercingFactory;
  @Mock
  private Target target;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Weapon weapon;

  @Before
  public void setUp() {
    weapon = new Weapon(eventBus, NAME, INITIAL_USAGE, ATTACK_POWER, piercingFactory);
  }

  @Test
  public void name() {
    assertThat(weapon.getName()).isEqualTo(NAME);
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

  @Test
  public void get_targets() {
    when(piercingFactory.create(enemy, weapon, Directions.UP))
        .thenReturn(Optional.<Target>absent());
    when(piercingFactory.create(enemy, weapon, Directions.DOWN))
        .thenReturn(Optional.<Target>absent());
    when(piercingFactory.create(enemy, weapon, Directions.LEFT))
        .thenReturn(Optional.<Target>absent());
    when(piercingFactory.create(enemy, weapon, Directions.RIGHT))
        .thenReturn(Optional.of(target));

    assertThat(weapon.createTargets(enemy)).containsExactly(target);
  }
}