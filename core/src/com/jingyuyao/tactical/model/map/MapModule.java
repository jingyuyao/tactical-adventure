package com.jingyuyao.tactical.model.map;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.World;

public class MapModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(EventBus.class);
    requireBinding(World.class);

    install(new FactoryModuleBuilder().build(CellFactory.class));
  }
}
