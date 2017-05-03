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

  /**
   * Return a list b/c eventually we will have multiple armors.
   */
  ImmutableList<Armor> getEquippedArmors();

  ImmutableList<Armor> getUnequippedArmors();

  void useConsumable(Consumable consumable);

  void useWeapon(Weapon weapon);

  /**
   * Plural b/c eventually we will have multiple armors.
   */
  void useEquippedArmors();

  void equipArmor(Armor armor);
}
