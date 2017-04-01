package com.jingyuyao.tactical.view;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.marking.MarkingsSubscriber;
import com.jingyuyao.tactical.view.ui.UISubscriber;
import com.jingyuyao.tactical.view.world.WorldSubscriber;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreenSubscriber {

  private final WorldSubscriber worldSubscriber;
  private final MarkingsSubscriber markingsSubscriber;
  private final UISubscriber uiSubscriber;

  @Inject
  WorldScreenSubscriber(
      WorldSubscriber worldSubscriber,
      MarkingsSubscriber markingsSubscriber,
      UISubscriber uiSubscriber) {
    this.worldSubscriber = worldSubscriber;
    this.markingsSubscriber = markingsSubscriber;
    this.uiSubscriber = uiSubscriber;
  }

  public void register(EventBus eventBus) {
    eventBus.register(worldSubscriber);
    eventBus.register(markingsSubscriber);
    eventBus.register(uiSubscriber);
  }
}
