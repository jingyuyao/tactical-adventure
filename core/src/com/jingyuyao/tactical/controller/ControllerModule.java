package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.Input;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.World;

public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Input.class);
    requireBinding(Model.class);
    requireBinding(World.class);

    install(new FactoryModuleBuilder().build(ControllerFactory.class));
  }
}
