package com.jingyuyao.tactical.model.character;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

public interface Character {

  /**
   * Return whether or not this character can currently be controlled by the player.
   */
  boolean canControl();

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

  ImmutableList<Armor> getStashedArmors();

  void useConsumable(Consumable consumable);

  void useWeapon(Weapon weapon);

  void useEquippedArmors();

  /**
   * Equips {@code armor} from the stash. Replaces the previously equipped armor of the same type if
   * its present.
   */
  void equipArmor(Armor armor);

  /**
   * Unequips equipped {@code armor} and move it to the stash.
   */
  void unequipArmor(Armor armor);
}
