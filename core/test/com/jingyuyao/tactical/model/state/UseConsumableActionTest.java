package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UseConsumableActionTest {

  @Mock
  private UsingConsumable usingConsumable;

  @Test
  public void run() {
    UseConsumableAction useConsumableAction = new UseConsumableAction(usingConsumable);

    useConsumableAction.run();

    verify(usingConsumable).use();
  }
}