package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.ship.Player;
import com.jingyuyao.tactical.model.world.Cell;
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
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Cell cell;
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
    when(cell.player()).thenReturn(Optional.of(player));
    usingConsumable = new UsingConsumable(modelBus, worldState, stateFactory, cell, consumable);
  }

  @Test
  public void enter() {
    usingConsumable.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isSameAs(usingConsumable);
  }

  @Test
  public void exit() {
    usingConsumable.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, usingConsumable, ExitState.class);
  }

  @Test
  public void use_consumable() {
    when(stateFactory.createWaiting()).thenReturn(waiting);

    usingConsumable.use();

    verify(consumable).apply(player);
    verify(player).useConsumable(consumable);
    verify(player).setControllable(false);
    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(Save.class);
    verify(worldState).branchTo(waiting);
  }

  @Test
  public void actions() {
    ImmutableList<Action> actions = usingConsumable.getActions();

    assertThat(actions).hasSize(2);
    assertThat(actions.get(0)).isInstanceOf(UseConsumableAction.class);
    assertThat(actions.get(1)).isInstanceOf(BackAction.class);
  }
}