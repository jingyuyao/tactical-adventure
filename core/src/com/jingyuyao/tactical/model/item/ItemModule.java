package com.jingyuyao.tactical.model.item;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;

public class ItemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Characters.class);
    requireBinding(Terrains.class);

    install(new FactoryModuleBuilder().build(TargetFactory.class));
  }
}
