package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.target.TargetFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// TODO: finish me
@RunWith(MockitoJUnitRunner.class)
public class MeleeTest {

  private static final String NAME = "ok";
  private static final int INITIAL_USAGE = 2;
  private static final int ATTACK_POWER = 5;

  @Mock
  private EventBus eventBus;
  @Mock
  private Terrains terrains;
  @Mock
  private TargetFactory targetFactory;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Melee melee;

  @Before
  public void setUp() {
    melee = new Melee(eventBus, NAME, INITIAL_USAGE, ATTACK_POWER, terrains, targetFactory);
  }

  @Test
  public void name() {
    assertThat(melee.getName()).isEqualTo(NAME);
  }

  @Test
  public void use() {
    melee.useOnce();

    verifyZeroInteractions(eventBus);

    melee.useOnce();
    verify(eventBus).post(argumentCaptor.capture());
    RemoveItem removeItem = TestHelpers.isInstanceOf(argumentCaptor.getValue(), RemoveItem.class);
    assertThat(removeItem.getObject()).isSameAs(melee);
  }
}