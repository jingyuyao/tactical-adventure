package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
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
  private Ship ship;
  @Mock
  private Consumable consumable;
  @Mock
  private Transition transition;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private UsingConsumable usingConsumable;

  @Before
  public void setUp() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(true);
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
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    usingConsumable.use();

    InOrder inOrder = Mockito.inOrder(consumable, ship, worldState, modelBus);
    inOrder.verify(consumable).apply(ship);
    inOrder.verify(ship).useConsumable(consumable);
    inOrder.verify(ship).setControllable(false);
    inOrder.verify(worldState).branchTo(transition);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    Save save = TestHelpers.assertClass(argumentCaptor.getValue(), Save.class);
    save.complete();
    inOrder.verify(worldState).branchTo(waiting);
  }

  @Test
  public void actions() {
    List<Action> actions = usingConsumable.getActions();

    assertThat(actions).hasSize(2);
    assertThat(actions.get(0)).isInstanceOf(UseConsumableAction.class);
    assertThat(actions.get(1)).isInstanceOf(BackAction.class);
  }
}