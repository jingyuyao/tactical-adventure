package com.jingyuyao.tactical.model;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelBus {

  private final EventBus eventBus;

  @Inject
  ModelBus(@ModelEventBus EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void post(Object event) {
    eventBus.post(event);
  }

  /**
   * Annotate a class with {@link Singleton} and {@link ModelBusListener} to register with this bus.
   */
  void register(Object object) {
    eventBus.register(object);
  }

  @Override
  public String toString() {
    return eventBus.toString();
  }
}
