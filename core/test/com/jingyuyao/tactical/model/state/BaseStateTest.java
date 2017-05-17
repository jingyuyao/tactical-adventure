package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ExitState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseStateTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private State anotherState;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private BaseState state;

  @Before
  public void setUp() {
    state = new BaseState(modelBus, worldState);
  }

  @Test
  public void enter() {
    state.enter();

    verify(modelBus).post(argumentCaptor.capture());
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(state);
  }

  @Test
  public void exit() {
    state.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, state, ExitState.class);
  }

  @Test
  public void go_to() {
    state.goTo(anotherState);

    verify(worldState).goTo(anotherState);
  }

  @Test
  public void back() {
    state.back();

    verify(worldState).back();
  }

  @Test
  public void roll_back() {
    state.rollback();

    verify(worldState).rollback();
  }

  @Test
  public void branch_to() {
    state.branchTo(anotherState);

    verify(worldState).branchTo(anotherState);
  }

  @Test
  public void remove_self() {
    state.removeSelf();

    verify(worldState).remove(state);
  }
}