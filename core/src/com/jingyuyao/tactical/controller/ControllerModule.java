package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ControllerFactory.class));

    bind(InputLock.class);
    bind(MapController.class);
    bind(DragCameraController.class);
  }
}
