package com.jingyuyao.tactical.view;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.ui.UISubscriber;
import com.jingyuyao.tactical.view.world2.WorldSubscriber;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreenSubscriber {

  private final WorldSubscriber worldSubscriber;
  private final UISubscriber uiSubscriber;

  @Inject
  WorldScreenSubscriber(
      WorldSubscriber worldSubscriber,
      UISubscriber uiSubscriber) {
    this.worldSubscriber = worldSubscriber;
    this.uiSubscriber = uiSubscriber;
  }

  public void register(EventBus eventBus) {
    eventBus.register(worldSubscriber);
    eventBus.register(uiSubscriber);
  }
}
