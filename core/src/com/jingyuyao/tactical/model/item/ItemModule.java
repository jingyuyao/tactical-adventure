package com.jingyuyao.tactical.model.item;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ItemModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().build(ItemFactory.class));
  }
}
