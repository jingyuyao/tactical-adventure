package com.jingyuyao.tactical.model.item;

import com.google.inject.AbstractModule;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.World;

public class ItemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(World.class);
    requireBinding(Movements.class);
  }
}
