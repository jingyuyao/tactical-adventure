package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.battle.TargetFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LaserTest {

  private static final String NAME = "ok";
  private static final int INITIAL_USAGE = 2;
  private static final int ATTACK_POWER = 5;
  private static final Coordinate COORDINATE = new Coordinate(10, 10);

  @Mock
  private EventBus eventBus;
  @Mock
  private Enemy enemy;
  @Mock
  private TargetFactory targetFactory;
  @Mock
  private Target target;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Laser laser;

  @Before
  public void setUp() {
    laser = new Laser(eventBus, NAME, INITIAL_USAGE, ATTACK_POWER, targetFactory);
  }

  @Test
  public void name() {
    assertThat(laser.getName()).isEqualTo(NAME);
  }

  @Test
  public void get_attack_power() {
    assertThat(laser.getAttackPower()).isEqualTo(ATTACK_POWER);
  }

  @Test
  public void use() {
    laser.useOnce();

    verifyZeroInteractions(eventBus);

    laser.useOnce();
    verify(eventBus).post(argumentCaptor.capture());
    RemoveItem removeItem = TestHelpers.isInstanceOf(argumentCaptor.getValue(), RemoveItem.class);
    assertThat(removeItem.getObject()).isSameAs(laser);
  }
}