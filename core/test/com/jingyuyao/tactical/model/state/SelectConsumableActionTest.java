package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.item.Consumable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectConsumableActionTest {

  @Mock
  private ControllingActionState playerActionState;
  @Mock
  private Consumable consumable;

  @Test
  public void run() {
    SelectConsumableAction selectConsumableAction =
        new SelectConsumableAction(playerActionState, consumable);

    selectConsumableAction.run();

    verify(playerActionState).selectConsumable(consumable);
  }
}