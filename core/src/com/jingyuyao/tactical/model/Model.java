package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.common.EventSubscriber;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Model {

  private final EventBus eventBus;
  private final Set<EventSubscriber> eventSubscribers;

  @Inject
  Model(@ModelEventBus EventBus eventBus, Set<EventSubscriber> eventSubscribers) {
    this.eventBus = eventBus;
    this.eventSubscribers = eventSubscribers;
  }

  /**
   * Registers all the {@link EventSubscriber} to the {@link EventBus}. {@link EventSubscriber}s are
   * found using {@link com.google.inject.multibindings.Multibinder} as each module contributes
   * the set of classes that needs to be registered.
   */
  public void registerEventSubscribers() {
    // Only singletons should be registered on model initialization, dynamically added classes
    // should add themselves to the EventBus.
    for (EventSubscriber subscriber : eventSubscribers) {
      Preconditions.checkState(subscriber.getClass().isAnnotationPresent(Singleton.class));
    }

    for (EventSubscriber subscriber : eventSubscribers) {
      eventBus.register(subscriber);
    }
  }
}
