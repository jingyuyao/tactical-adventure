package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.item.Consumable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsingConsumableTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Consumable consumable;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private UsingConsumable usingConsumable;

  @Before
  public void setUp() {
    usingConsumable = new UsingConsumable(eventBus, mapState, stateFactory, player, consumable);
  }

  @Test
  public void enter() {
    usingConsumable.enter();

    verify(eventBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(usingConsumable);
  }

  @Test
  public void exit() {
    usingConsumable.exit();

    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, usingConsumable, ExitState.class);
  }

  @Test
  public void use_consumable() {
    when(stateFactory.createWaiting()).thenReturn(waiting);

    usingConsumable.use();

    verify(player).quickAccess(consumable);
    verify(consumable).apply(player);
    verify(player).useItem(consumable);
    verify(player).setActionable(false);
    verify(mapState).branchTo(waiting);
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = usingConsumable.getActions();

    assertThat(actions).hasSize(2);
    assertThat(actions.get(0)).isInstanceOf(UseConsumableAction.class);
    assertThat(actions.get(1)).isInstanceOf(BackAction.class);
  }
}