package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FinishActionTest {

  @Mock
  private ControllingState playerState;

  @Test
  public void run() {
    FinishAction action = new FinishAction(playerState);

    action.run();

    verify(playerState).finish();
  }
}