package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.World;
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
public class WaitingTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private World world;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Turn turn;
  @Mock
  private Cell cell;
  @Mock
  private Ship playerShip;
  @Mock
  private Transition transition;
  @Mock
  private Moving moving;
  @Mock
  private EndTurn endTurn;
  @Mock
  private Movement movement;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(modelBus, worldState, world, stateFactory);
  }

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.ENEMY);

    waiting.enter();
  }

  @Test
  public void enter() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);

    waiting.enter();
  }

  @Test
  public void exit() {
    waiting.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, waiting, ExitState.class);
  }

  @Test
  public void select_player_controllable() {
    when(world.getShipMovement(cell)).thenReturn(movement);
    when(stateFactory.createMoving(cell, movement)).thenReturn(moving);
    when(cell.ship()).thenReturn(Optional.of(playerShip));
    when(playerShip.isControllable()).thenReturn(true);

    waiting.select(cell);

    verify(stateFactory).createMoving(cell, movement);
    verify(worldState).goTo(moving);
  }

  @Test
  public void select_player_not_controllable() {
    when(cell.ship()).thenReturn(Optional.of(playerShip));
    when(playerShip.isControllable()).thenReturn(false);

    waiting.select(cell);

    verifyZeroInteractions(stateFactory);
    verifyZeroInteractions(worldState);
  }

  @Test
  public void end_turn() {
    when(stateFactory.createTransition()).thenReturn(transition);
    when(stateFactory.createEndTurn()).thenReturn(endTurn);

    waiting.endTurn();

    InOrder inOrder = Mockito.inOrder(worldState, modelBus);
    inOrder.verify(worldState).advanceTurn();
    inOrder.verify(worldState).branchTo(transition);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    Save save = TestHelpers.assertClass(argumentCaptor.getValue(), Save.class);
    save.complete();
    inOrder.verify(worldState).branchTo(endTurn);
  }

  @Test
  public void actions() {
    List<Action> actions = waiting.getActions();

    assertThat(actions).hasSize(1);
    assertThat(actions.get(0)).isInstanceOf(EndTurnAction.class);
  }
}
