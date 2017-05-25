package com.jingyuyao.tactical.model.ship;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Internal representation of all the {@link Item} a {@link Ship} holds.
 */
class Items implements Serializable {

  private List<Consumable> consumables;
  private List<Weapon> weapons;
  /**
   * Invariant: contains at most one of each class of armor.
   */
  private List<Armor> equippedArmors;
  private List<Armor> stashedArmors;

  Items() {
  }

  Items(
      List<Consumable> consumables,
      List<Weapon> weapons,
      List<Armor> equippedArmors,
      List<Armor> stashedArmors) {
    this.consumables = consumables;
    this.weapons = weapons;
    this.stashedArmors = stashedArmors;
    this.equippedArmors = equippedArmors;
  }

  List<Consumable> getConsumables() {
    return consumables;
  }

  List<Weapon> getWeapons() {
    return weapons;
  }

  List<Armor> getEquippedArmors() {
    return equippedArmors;
  }

  List<Armor> getStashedArmors() {
    return stashedArmors;
  }

  int getDefense() {
    int defense = 0;
    for (Armor armor : getEquippedArmors()) {
      defense += armor.getDefense();
    }
    return defense;
  }

  void useConsumable(Consumable consumable) {
    useItem(consumable, consumables);
  }

  void useWeapon(Weapon weapon) {
    useItem(weapon, weapons);
  }

  void useEquippedArmors() {
    for (Iterator<Armor> iterator = equippedArmors.iterator(); iterator.hasNext(); ) {
      Armor armor = iterator.next();
      armor.useOnce();
      if (armor.getUsageLeft() == 0) {
        iterator.remove();
      }
    }
  }

  void equipArmor(Armor armor) {
    Preconditions.checkNotNull(armor);
    Preconditions.checkArgument(stashedArmors.remove(armor));
    Optional<Armor> prevEquippedOpt =
        Iterables.tryFind(equippedArmors, Predicates.instanceOf(armor.getClass()));
    for (Armor prevEquipped : prevEquippedOpt.asSet()) {
      equippedArmors.remove(prevEquipped);
      stashedArmors.add(prevEquipped);
    }
    equippedArmors.add(armor);
  }

  void unequipArmor(Armor armor) {
    Preconditions.checkNotNull(armor);
    Preconditions.checkArgument(equippedArmors.remove(armor));
    stashedArmors.add(armor);
  }

  private <T extends Item> void useItem(T item, List<T> containingList) {
    Preconditions.checkNotNull(item);
    Preconditions.checkArgument(containingList.contains(item));
    item.useOnce();
    if (item.getUsageLeft() == 0) {
      containingList.remove(item);
    }
  }
}
