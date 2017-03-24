package com.jingyuyao.tactical.model.map;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.World;

public class MapModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(World.class);
  }
}
