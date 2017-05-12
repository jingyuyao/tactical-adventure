package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.ModelBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StartTurnTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Waiting waiting;

  private StartTurn startTurn;

  @Before
  public void setUp() {
    startTurn = new StartTurn(modelBus, worldState, stateFactory);
  }

  @Test
  public void enter() {
    when(stateFactory.createWaiting()).thenReturn(waiting);

    startTurn.enter();

    verify(worldState).branchTo(waiting);
  }
}