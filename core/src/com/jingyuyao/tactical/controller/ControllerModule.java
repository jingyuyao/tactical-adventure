package com.jingyuyao.tactical.controller;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldView;

public class ControllerModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(World.class);
    requireBinding(WorldState.class);
    requireBinding(WorldView.class);
  }
}
