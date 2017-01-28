package com.jingyuyao.tactical.model.item;

public interface ItemFactory {

  DirectionalWeapon create(DirectionalWeaponData weaponStats);

  Grenade create(GrenadeData grenadeStats);

  Heal create(HealData healData);
}
