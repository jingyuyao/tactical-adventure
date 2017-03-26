package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.world.Cell;
import java.util.Deque;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldStateTest {

  @Mock
  private Deque<State> stateStack;
  @Mock
  private State state1;
  @Mock
  private State state2;
  @Mock
  private State state3;
  @Mock
  private Cell cell;

  private WorldState worldState;

  @Before
  public void setUp() {
    worldState = new WorldState(stateStack);
  }

  @Test
  public void initialize() throws Exception {
    worldState.initialize(state1);

    verify(stateStack).push(state1);
    verify(state1).enter();
  }

  @Test
  public void reset() {
    when(stateStack.peek()).thenReturn(state1);

    worldState.reset();

    verify(state1).exit();
    verify(stateStack).clear();
  }

  @Test
  public void select_cell() {
    when(stateStack.peek()).thenReturn(state1);

    worldState.select(cell);

    verify(state1).select(cell);
  }

  @Test
  public void go_to() {
    when(stateStack.peek()).thenReturn(state1);

    worldState.goTo(state2);

    InOrder inOrder = inOrder(state1, state2, stateStack);
    inOrder.verify(state1).exit();
    inOrder.verify(stateStack).push(state2);
    inOrder.verify(state2).enter();
  }

  @Test
  public void back_nothing() {
    when(stateStack.size()).thenReturn(1);

    worldState.back();

    verify(stateStack).size();
    verifyNoMoreInteractions(stateStack);
  }

  @Test
  public void back() {
    when(stateStack.size()).thenReturn(2);
    when(stateStack.pop()).thenReturn(state2);
    when(stateStack.peek()).thenReturn(state1);

    worldState.back();

    InOrder inOrder = inOrder(stateStack, state2, state1);

    inOrder.verify(stateStack).pop();
    inOrder.verify(state2).exit();
    inOrder.verify(stateStack).peek();
    inOrder.verify(state1).canceled();
    inOrder.verify(state1).enter();
  }

  @Test
  public void rollback_nothing() {
    when(stateStack.size()).thenReturn(1);

    worldState.rollback();

    verify(stateStack).size();
    verifyNoMoreInteractions(stateStack);
  }

  @Test
  public void rollback() {
    when(stateStack.size()).thenReturn(3, 2, 1);
    when(stateStack.pop()).thenReturn(state3, state2);
    when(stateStack.peek()).thenReturn(state2, state1);

    worldState.rollback();

    InOrder inOrder = inOrder(stateStack, state3, state2, state1);

    // oh boy...
    inOrder.verify(stateStack).pop();
    inOrder.verify(state3).exit();
    inOrder.verify(stateStack).peek();
    inOrder.verify(state2).canceled();
    inOrder.verify(state2).enter();

    inOrder.verify(stateStack).pop();
    inOrder.verify(state2).exit();
    inOrder.verify(stateStack).peek();
    inOrder.verify(state1).canceled();
    inOrder.verify(state1).enter();
  }

  @Test
  public void newStack() {
    when(stateStack.peek()).thenReturn(state2);

    worldState.branchTo(state1);

    InOrder inOrder = inOrder(stateStack, state1, state2);

    inOrder.verify(stateStack).peek();
    inOrder.verify(state2).exit();
    inOrder.verify(stateStack).clear();
    inOrder.verify(stateStack).push(state1);
    inOrder.verify(state1).enter();
  }

  @Test
  public void pop() {
    worldState.popLast();

    verify(stateStack).pop();
  }
}
