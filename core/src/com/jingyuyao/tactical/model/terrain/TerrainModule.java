package com.jingyuyao.tactical.model.terrain;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class TerrainModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(TerrainFactory.class));
  }
}
