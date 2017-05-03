package com.jingyuyao.tactical.model.character;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

public interface Character {

  String getName();

  /**
   * Always >= 0.
   */
  int getHp();

  int getMoveDistance();

  /**
   * Sum of all the equipped armors' defense
   */
  int getDefense();

  void damageBy(int delta);

  void healBy(int delta);

  void fullHeal();

  ImmutableList<Consumable> getConsumables();

  ImmutableList<Weapon> getWeapons();

  ImmutableList<Armor> getEquippedArmors();

  ImmutableList<Armor> getUnequippedArmors();

  void useConsumable(Consumable consumable);

  void useWeapon(Weapon weapon);

  void useEquippedArmors();

  /**
   * Equips {@code armor}. Replaces the previously equipped armor of the same type if its present.
   */
  void equipArmor(Armor armor);

  /**
   * Unequips {@code armor}. {@code armor} must be currently equipped
   */
  void unequipArmor(Armor armor);
}
