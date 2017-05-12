package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.world.Cell;
import java.util.Deque;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldStateTest {

  @Mock
  private StateFactory stateFactory;
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

  private Deque<State> stateStack;
  private WorldState worldState;

  @Before
  public void setUp() {
    stateStack = new LinkedList<>();
    worldState = new WorldState(stateFactory, stateStack);
  }

  @Test
  public void initialize() {
    when(stateFactory.createStartTurn()).thenReturn(startTurn);

    worldState.initialize(11);

    assertThat(worldState.getTurn()).isEqualTo(11);
    assertThat(stateStack).containsExactly(startTurn);
    verify(startTurn).enter();
  }

  @Test
  public void reset() {
    stateStack.push(state1);

    worldState.reset();

    verify(state1).exit();
    assertThat(stateStack).isEmpty();
    assertThat(worldState.getTurn()).isEqualTo(1);
  }

  @Test
  public void increment_turn() {
    when(stateFactory.createStartTurn()).thenReturn(startTurn);

    worldState.initialize(11);
    worldState.incrementTurn();

    assertThat(worldState.getTurn()).isEqualTo(12);
  }

  @Test
  public void select_cell() {
    stateStack.push(state1);

    worldState.select(cell);

    verify(state1).select(cell);
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
