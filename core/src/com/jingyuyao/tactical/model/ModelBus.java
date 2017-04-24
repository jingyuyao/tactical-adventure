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

  public void register(Object object) {
    eventBus.register(object);
  }

  public void unregister(Object object) {
    eventBus.unregister(object);
  }

  public String identifier() {
    return eventBus.identifier();
  }

  public void post(Object event) {
    eventBus.post(event);
  }

  @Override
  public String toString() {
    return eventBus.toString();
  }
}
