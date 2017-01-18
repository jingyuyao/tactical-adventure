package com.jingyuyao.tactical.model.item;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.target.TargetFactory;

public class ItemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Terrains.class);
    requireBinding(TargetFactory.class);

    install(new FactoryModuleBuilder()
        .implement(Weapon.class, Names.named("Melee"), Melee.class)
        .implement(Weapon.class, Names.named("PiercingLaser"), PiercingLaser.class)
        .implement(Consumable.class, Names.named("Heal"), Heal.class)
        .build(ItemFactory.class));
  }
}
