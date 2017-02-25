package com.jingyuyao.tactical.view;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.marking.MarkingSubscriber;
import com.jingyuyao.tactical.view.ui.UISubscriber;
import com.jingyuyao.tactical.view.world.WorldSubscriber;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreenSubscribers {

  private final WorldSubscriber worldSubscriber;
  private final MarkingSubscriber markingSubscriber;
  private final UISubscriber uiSubscriber;

  @Inject
  WorldScreenSubscribers(
      WorldSubscriber worldSubscriber,
      MarkingSubscriber markingSubscriber,
      UISubscriber uiSubscriber) {
    this.worldSubscriber = worldSubscriber;
    this.markingSubscriber = markingSubscriber;
    this.uiSubscriber = uiSubscriber;
  }

  public void register(EventBus eventBus) {
    eventBus.register(worldSubscriber);
    eventBus.register(markingSubscriber);
    eventBus.register(uiSubscriber);
  }
}
