package com.jingyuyao.tactical.model.character;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.Iterator;
import java.util.List;

/**
 * Internal representation of all the {@link Item} a {@link Character} holds.
 */
class Items {

  private List<Consumable> consumables;
  private List<Weapon> weapons;
  /**
   * Invariant: contains at most one of each type of armor.
   */
  private List<Armor> equippedArmors;
  private List<Armor> unequippedArmors;

  Items(
      List<Consumable> consumables,
      List<Weapon> weapons,
      List<Armor> equippedArmors,
      List<Armor> unequippedArmors) {
    this.consumables = consumables;
    this.weapons = weapons;
    this.unequippedArmors = unequippedArmors;
    this.equippedArmors = equippedArmors;
  }

  ImmutableList<Consumable> getConsumables() {
    return ImmutableList.copyOf(consumables);
  }

  ImmutableList<Weapon> getWeapons() {
    return ImmutableList.copyOf(weapons);
  }

  ImmutableList<Armor> getEquippedArmors() {
    return ImmutableList.copyOf(equippedArmors);
  }

  ImmutableList<Armor> getUnequippedArmors() {
    return ImmutableList.copyOf(unequippedArmors);
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
    Preconditions.checkArgument(unequippedArmors.remove(armor));
    Optional<Armor> prevEquippedOpt =
        Iterables.tryFind(equippedArmors, Predicates.instanceOf(armor.getClass()));
    for (Armor prevEquipped : prevEquippedOpt.asSet()) {
      equippedArmors.remove(prevEquipped);
      unequippedArmors.add(prevEquipped);
    }
    equippedArmors.add(armor);
  }

  void unequipArmor(Armor armor) {
    Preconditions.checkNotNull(armor);
    Preconditions.checkArgument(equippedArmors.remove(armor));
    unequippedArmors.add(armor);
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
