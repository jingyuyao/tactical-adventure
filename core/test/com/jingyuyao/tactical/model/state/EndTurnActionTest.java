package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EndTurnActionTest {

  @Mock
  private Waiting waiting;

  @Test
  public void run() {
    EndTurnAction action = new EndTurnAction(waiting);

    action.run();

    verify(waiting).endTurn();
  }
}