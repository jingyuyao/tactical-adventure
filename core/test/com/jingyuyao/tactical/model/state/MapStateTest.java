package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.SelectCharacter;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Deque;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapStateTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private Deque<State> stateStack;
  @Mock
  private State state1;
  @Mock
  private State state2;
  @Mock
  private State state3;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private MapState mapState;

  @Before
  public void setUp() {
    mapState = new MapState(eventBus, stateStack);
  }

  @Test
  public void initialize() throws Exception {
    mapState.initialize(state1);

    verify(stateStack).push(state1);
    verify(state1).enter();
    verify(eventBus).post(argumentCaptor.capture());
    verifyStateChangedTo(state1);
  }

  @Test
  public void selectPlayer() throws Exception {
    when(stateStack.peek()).thenReturn(state1);

    mapState.select(player);

    verify(state1).select(player);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, player, SelectCharacter.class);
  }

  @Test
  public void selectEnemy() throws Exception {
    when(stateStack.peek()).thenReturn(state1);

    mapState.select(enemy);

    verify(state1).select(enemy);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, enemy, SelectCharacter.class);
  }

  @Test
  public void selectTerrain() throws Exception {
    when(stateStack.peek()).thenReturn(state1);

    mapState.select(terrain);

    verify(state1).select(terrain);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, terrain, SelectTerrain.class);
  }

  @Test
  public void go_to() throws Exception {
    when(stateStack.peek()).thenReturn(state1);

    mapState.goTo(state2);

    InOrder inOrder = inOrder(state1, state2, stateStack, eventBus);
    inOrder.verify(state1).exit();
    inOrder.verify(stateStack).push(state2);
    inOrder.verify(eventBus).post(argumentCaptor.capture());
    verifyStateChangedTo(state2);
    inOrder.verify(state2).enter();
  }

  @Test
  public void back_nothing() throws Exception {
    when(stateStack.size()).thenReturn(1);

    mapState.back();

    verify(stateStack).size();
    verifyNoMoreInteractions(stateStack);
    verifyZeroInteractions(eventBus);
  }

  @Test
  public void back() throws Exception {
    when(stateStack.size()).thenReturn(2);
    when(stateStack.pop()).thenReturn(state2);
    when(stateStack.peek()).thenReturn(state1);

    mapState.back();

    InOrder inOrder = inOrder(stateStack, state2, state1, eventBus);

    inOrder.verify(stateStack).pop();
    inOrder.verify(state2).exit();
    inOrder.verify(stateStack).peek();
    inOrder.verify(state1).canceled();
    inOrder.verify(eventBus).post(argumentCaptor.capture());
    inOrder.verify(state1).enter();
    verifyStateChangedTo(state1);
  }

  @Test
  public void rollback_nothing() throws Exception {
    when(stateStack.size()).thenReturn(1);

    mapState.rollback();

    verify(stateStack).size();
    verifyNoMoreInteractions(stateStack);
    verifyZeroInteractions(eventBus);
  }

  @Test
  public void rollback() throws Exception {
    when(stateStack.size()).thenReturn(3, 2, 1);
    when(stateStack.pop()).thenReturn(state3, state2);
    when(stateStack.peek()).thenReturn(state2, state1);

    mapState.rollback();

    InOrder inOrder = inOrder(stateStack, state3, state2, state1, eventBus);

    // oh boy...
    inOrder.verify(stateStack).pop();
    inOrder.verify(state3).exit();
    inOrder.verify(stateStack).peek();
    inOrder.verify(state2).canceled();
    inOrder.verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, state2, StateChanged.class);
    inOrder.verify(state2).enter();

    inOrder.verify(stateStack).pop();
    inOrder.verify(state2).exit();
    inOrder.verify(stateStack).peek();
    inOrder.verify(state1).canceled();
    inOrder.verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, state1, StateChanged.class);
    inOrder.verify(state1).enter();
  }

  @Test
  public void newStack() throws Exception {
    when(stateStack.peek()).thenReturn(state2);

    mapState.branchTo(state1);

    InOrder inOrder = inOrder(stateStack, state1, state2, eventBus);

    inOrder.verify(stateStack).peek();
    inOrder.verify(state2).exit();
    inOrder.verify(stateStack).clear();
    inOrder.verify(stateStack).push(state1);
    inOrder.verify(eventBus).post(argumentCaptor.capture());
    inOrder.verify(state1).enter();
    verifyStateChangedTo(state1);
  }

  @Test
  public void pop() {
    mapState.popLast();

    verify(stateStack).pop();
  }

  private void verifyStateChangedTo(State targetState) {
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, targetState, StateChanged.class);
  }
}
