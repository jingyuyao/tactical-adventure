package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AttackActionTest {

  @Mock
  private Battling battling;

  @Test
  public void run() {
    AttackAction attackAction = new AttackAction(battling);

    attackAction.run();

    verify(battling).attack();
  }
}