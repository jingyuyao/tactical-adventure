package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;

public interface ItemFactory {

  DirectionalWeapon createDirectionalWeapon(Character owner, DirectionalWeaponStats weaponStats);

  Grenade createGrenade(Character owner, GrenadeStats grenadeStats);

  Heal createHeal(Character owner, ItemStats itemStats);
}
