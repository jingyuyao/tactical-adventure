package com.jingyuyao.tactical.model.item;

public interface ItemFactory {

  DirectionalWeapon createDirectionalWeapon(DirectionalWeaponData weaponStats);

  Grenade createGrenade(GrenadeData grenadeStats);

  Heal createHeal(HealData healData);
}
