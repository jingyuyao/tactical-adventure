package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.world.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransitionTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private WorldState worldState;
  @Mock
  private Cell cell;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Transition transition;

  @Before
  public void setUp() {
    transition = new Transition(eventBus, worldState);
  }

  @Test
  public void enter() {
    transition.enter();

    verify(eventBus).post(transition);
  }

  @Test
  public void exit() {
    transition.exit();

    verify(worldState).popLast();
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, transition, ExitState.class);
  }

  @Test
  public void select() {
    transition.select(cell);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void actions() {
    assertThat(transition.getActions()).isEmpty();
  }
}