package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.event.RemoveItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HealTest {

  private static final String NAME = "pot";
  private static final int INITIAL_USAGE = 2;

  @Mock
  private EventBus eventBus;
  @Mock
  private Player player;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Heal heal;

  @Before
  public void setUp() {
    heal = new Heal(eventBus, NAME, INITIAL_USAGE);
  }

  @Test
  public void consume() {
    heal.consume(player);
    heal.consume(player);

    verify(player, times(2)).healBy(Heal.AMOUNT);
    verify(eventBus).post(argumentCaptor.capture());
    RemoveItem removeItem = TestHelpers.isInstanceOf(argumentCaptor.getValue(), RemoveItem.class);
    assertThat(removeItem.getObject()).isSameAs(heal);
  }
}