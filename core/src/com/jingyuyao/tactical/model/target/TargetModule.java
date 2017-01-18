package com.jingyuyao.tactical.model.target;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;

public class TargetModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Characters.class);
    requireBinding(Terrains.class);

    install(new FactoryModuleBuilder()
        .implement(Target.class, Names.named("ConstantDamage"), ConstantDamage.class)
        .build(TargetFactory.class));
  }
}
