package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ControllingStateTest {

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
  private Transition transition;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  @Test
  public void good_cell() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(true);
    new ControllingState(modelBus, worldState, stateFactory, cell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void no_ship() {
    when(cell.ship()).thenReturn(Optional.<Ship>absent());
    new ControllingState(modelBus, worldState, stateFactory, cell);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cant_control() {
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(false);
    new ControllingState(modelBus, worldState, stateFactory, cell);
  }

  @Test
  public void finish() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createWaiting()).thenReturn(waiting);
    when(cell.ship()).thenReturn(Optional.of(ship));
    when(ship.isControllable()).thenReturn(true);
    ControllingState state = new ControllingState(modelBus, worldState, stateFactory, cell);

    state.finish();

    InOrder inOrder = Mockito.inOrder(ship, modelBus, worldState);
    inOrder.verify(ship).setControllable(false);
    inOrder.verify(worldState).branchTo(transition);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    Save save = TestHelpers.assertClass(argumentCaptor.getValue(), Save.class);
    save.complete();
    inOrder.verify(worldState).branchTo(waiting);
  }
}