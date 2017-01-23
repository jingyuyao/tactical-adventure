package com.jingyuyao.tactical.model.item;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;

public class ItemModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Characters.class);
    requireBinding(Terrains.class);

    install(new FactoryModuleBuilder()
        .implement(Weapon.class, Names.named("DirectionalWeapon"), DirectionalWeapon.class)
        .implement(Consumable.class, Names.named("Heal"), Heal.class)
        .build(ItemFactory.class));
    install(new FactoryModuleBuilder().build(TargetFactory.class));
  }
}
