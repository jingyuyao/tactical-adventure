package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackActionTest {

  @Mock
  private BaseState baseState;

  @Test
  public void run() {
    BackAction backAction = new BackAction(baseState);

    backAction.run();

    verify(baseState).back();
  }
}