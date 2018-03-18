package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Deque;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldStateTest {

  @Mock
  private StateFactory stateFactory;
  @Mock
  private ModelBus modelBus;
  @Mock
  private State state1;
  @Mock
  private State state2;
  @Mock
  private State state3;
  @Mock
  private Cell cell;
  @Mock
  private StartTurn startTurn;
  @Mock
  private Waiting waiting;
  @Mock
  private EndTurn endTurn;
  @Mock
  private Retaliating retaliating;
  @Mock
  private Turn turn;
  @Mock
  private Turn nextTurn;
  @Mock
  private Script script;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Deque<State> stateStack;
  private WorldState worldState;

  @Before
  public void setUp() {
    stateStack = new LinkedList<>();
    worldState = new WorldState(stateFactory, stateStack, modelBus);
  }

  @Test
  public void initialize_to_start() {
    when(turn.getStage()).thenReturn(TurnStage.START);
    when(stateFactory.createStartTurn()).thenReturn(startTurn);

    worldState.initialize(turn, script);

    assertThat(worldState.getTurn()).isSameAs(turn);
    assertThat(worldState.getScript()).isSameAs(script);
    assertThat(stateStack).containsExactly(startTurn);
    verify(startTurn).enter();
  }

  @Test
  public void initialize_to_player() {
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    worldState.initialize(turn, script);

    assertThat(worldState.getTurn()).isSameAs(turn);
    assertThat(worldState.getScript()).isSameAs(script);
    assertThat(stateStack).containsExactly(waiting);
    verify(waiting).enter();
  }

  @Test
  public void initialize_to_end() {
    when(turn.getStage()).thenReturn(TurnStage.END);
    when(stateFactory.createEndTurn()).thenReturn(endTurn);

    worldState.initialize(turn, script);

    assertThat(worldState.getTurn()).isSameAs(turn);
    assertThat(worldState.getScript()).isSameAs(script);
    assertThat(stateStack).containsExactly(endTurn);
    verify(endTurn).enter();
  }

  @Test
  public void initialize_to_enemy() {
    when(turn.getStage()).thenReturn(TurnStage.ENEMY);
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    worldState.initialize(turn, script);

    assertThat(worldState.getTurn()).isSameAs(turn);
    assertThat(worldState.getScript()).isSameAs(script);
    assertThat(stateStack).containsExactly(retaliating);
    verify(retaliating).enter();
  }

  @Test
  public void reset() {
    stateStack.push(state1);

    worldState.reset();

    verify(state1).exit();
    assertThat(stateStack).isEmpty();
    assertThat(worldState.getTurn()).isNull();
    assertThat(worldState.getScript()).isNull();
  }

  @Test
  public void reset_not_initialized() {
    worldState.reset();

    assertThat(stateStack).isEmpty();
    assertThat(worldState.getTurn()).isNull();
    assertThat(worldState.getScript()).isNull();
  }

  @Test
  public void select_cell() {
    stateStack.push(state1);

    worldState.select(cell);

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SelectCell.class);
    verify(state1).select(cell);
  }

  @Test
  public void advance_turn() {
    initialize_to_player();
    when(turn.advance()).thenReturn(nextTurn);

    worldState.advanceTurn();

    assertThat(worldState.getTurn()).isSameAs(nextTurn);
  }

  @Test
  public void go_to() {
    stateStack.push(state1);

    worldState.goTo(state2);

    InOrder inOrder = inOrder(state1, state2);
    inOrder.verify(state1).exit();
    inOrder.verify(state2).enter();
    assertThat(stateStack).containsExactly(state2, state1).inOrder();
  }

  @Test
  public void back_nothing() {
    worldState.back();

    assertThat(stateStack).isEmpty();
  }

  @Test
  public void back() {
    stateStack.push(state1);
    stateStack.push(state2);

    worldState.back();

    InOrder inOrder = inOrder(state1, state2);

    inOrder.verify(state2).exit();
    inOrder.verify(state1).canceled();
    inOrder.verify(state1).enter();
    assertThat(stateStack).containsExactly(state1);
  }

  @Test
  public void rollback_nothing() {
    worldState.rollback();

    assertThat(stateStack).isEmpty();
  }

  @Test
  public void rollback() {
    stateStack.push(state1);
    stateStack.push(state2);
    stateStack.push(state3);

    worldState.rollback();

    InOrder inOrder = inOrder(state3, state2, state1);

    inOrder.verify(state3).exit();
    inOrder.verify(state2).canceled();
    inOrder.verify(state2).enter();

    inOrder.verify(state2).exit();
    inOrder.verify(state1).canceled();
    inOrder.verify(state1).enter();

    assertThat(stateStack).containsExactly(state1);
  }

  @Test
  public void newStack() {
    stateStack.push(state2);
    stateStack.push(state3);

    worldState.branchTo(state1);

    InOrder inOrder = inOrder(state1, state3);

    inOrder.verify(state3).exit();
    inOrder.verify(state1).enter();
    assertThat(stateStack).containsExactly(state1);
  }

  @Test
  public void remove() {
    stateStack.push(state1);
    stateStack.push(state2);

    worldState.remove(state2);

    assertThat(stateStack).containsExactly(state1);
  }
}
