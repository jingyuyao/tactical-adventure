package com.jingyuyao.tactical.view;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.ui.UISubscriber;
import com.jingyuyao.tactical.view.world2.WorldSubscriber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldScreenSubscriberTest {

  @Mock
  private WorldSubscriber worldSubscriber;
  @Mock
  private UISubscriber uiSubscriber;
  @Mock
  private EventBus eventBus;

  @Test
  public void register_subscribers() {
    WorldScreenSubscriber worldScreenSubscriber =
        new WorldScreenSubscriber(worldSubscriber, uiSubscriber);

    worldScreenSubscriber.register(eventBus);

    InOrder inOrder = Mockito.inOrder(eventBus);
    inOrder.verify(eventBus).register(worldSubscriber);
    inOrder.verify(eventBus).register(uiSubscriber);
  }
}