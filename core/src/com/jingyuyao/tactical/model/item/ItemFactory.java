package com.jingyuyao.tactical.model.item;

import com.google.inject.name.Named;

public interface ItemFactory {

  @Named("Melee")
  Weapon createMelee(WeaponStats weaponInfo);

  @Named("PiercingLaser")
  Weapon createPiercingLaser(WeaponStats weaponInfo);

  @Named("Heal")
  Consumable createHeal(ItemStats itemStats);
}
