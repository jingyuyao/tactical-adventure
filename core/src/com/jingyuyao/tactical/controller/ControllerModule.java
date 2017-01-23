package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.jingyuyao.tactical.model.common.EventSubscriber;

public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ControllerFactory.class));

    Multibinder<EventSubscriber> subscriberBinder =
        Multibinder.newSetBinder(binder(), EventSubscriber.class);
    subscriberBinder.addBinding().to(DragCameraController.class);
    subscriberBinder.addBinding().to(MapController.class);
  }
}
