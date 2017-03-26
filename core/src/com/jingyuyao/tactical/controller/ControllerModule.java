package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.map.World;

public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Model.class);
    requireBinding(World.class);

    install(new FactoryModuleBuilder().build(ControllerFactory.class));
  }
}
