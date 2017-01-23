package com.jingyuyao.tactical.model.item;

import com.google.inject.name.Named;

public interface ItemFactory {

  @Named("DirectionalWeapon")
  Weapon createDirectionalWeapon(DirectionalWeaponStats weaponStats);

  @Named("Heal")
  Consumable createHeal(ItemStats itemStats);
}
