package com.jingyuyao.tactical.model.item;

import com.google.inject.name.Named;
import com.jingyuyao.tactical.model.character.Character;

public interface ItemFactory {

  @Named("DirectionalWeapon")
  Weapon createDirectionalWeapon(Character owner, DirectionalWeaponStats weaponStats);

  @Named("Grenade")
  Weapon createGrenade(Character owner, GrenadeStats grenadeStats);

  @Named("Heal")
  Consumable createHeal(Character owner, ItemStats itemStats);
}
