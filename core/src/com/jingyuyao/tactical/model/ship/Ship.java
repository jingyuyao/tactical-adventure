package com.jingyuyao.tactical.model.ship;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

public interface Ship {

  ResourceKey getAnimation();

  ResourceKey getName();

  Allegiance getAllegiance();

  /**
   * Return whether or not this ship can currently be controlled by the player.
   */
  boolean isControllable();

  void setControllable(boolean controllable);

  AutoPilot getAutoPilot(Movements movements, Cell starting);

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

  ImmutableList<Person> getPilots();

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
