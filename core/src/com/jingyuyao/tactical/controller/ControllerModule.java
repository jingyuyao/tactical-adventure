package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.SelectionHandler;

public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(SelectionHandler.class);
    requireBinding(Terrains.class);

    install(new FactoryModuleBuilder().build(ControllerFactory.class));
  }
}
