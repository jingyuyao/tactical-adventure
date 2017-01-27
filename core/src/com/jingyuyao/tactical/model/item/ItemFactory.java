package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;

public interface ItemFactory {

  DirectionalWeapon createDirectionalWeapon(Character owner, DirectionalWeaponData weaponStats);

  Grenade createGrenade(Character owner, GrenadeData grenadeStats);

  Heal createHeal(Character owner, ItemData itemData);
}
