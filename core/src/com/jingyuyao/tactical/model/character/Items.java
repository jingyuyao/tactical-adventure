package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.List;

/**
 * Internal representation of all the {@link Item} a {@link Character} holds.
 */
class Items {

  private List<Consumable> consumables;
  private List<Weapon> weapons;
  private List<Armor> unequippedArmors;
  private Armor bodyArmor;

  Items(
      List<Consumable> consumables,
      List<Weapon> weapons,
      List<Armor> unequippedArmors,
      Armor bodyArmor) {
    this.consumables = consumables;
    this.weapons = weapons;
    this.unequippedArmors = unequippedArmors;
    this.bodyArmor = bodyArmor;
  }

  @SafeVarargs // pointless warning, Guava disables it as well.
  private static <T> ImmutableList<T> ignoreNullOf(T... things) {
    Builder<T> builder = new Builder<>();
    for (T thing : things) {
      if (thing != null) {
        builder.add(thing);
      }
    }
    return builder.build();
  }

  ImmutableList<Consumable> getConsumables() {
    return ImmutableList.copyOf(consumables);
  }

  ImmutableList<Weapon> getWeapons() {
    return ImmutableList.copyOf(weapons);
  }

  ImmutableList<Armor> getEquippedArmors() {
    return ignoreNullOf(bodyArmor);
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
    bodyArmor = useEquipped(bodyArmor);
  }

  void equipBodyArmor(Armor armor) {
    bodyArmor = equipItem(bodyArmor, armor, unequippedArmors);
  }

  private synchronized <T extends Item> void useItem(T item, List<T> containingList) {
    Preconditions.checkNotNull(item);
    Preconditions.checkArgument(containingList.contains(item));
    item.useOnce();
    if (item.getUsageLeft() == 0) {
      containingList.remove(item);
    }
  }

  /**
   * Use the given item if its not null. Return the item if it still has usage left, null otherwise.
   */
  private synchronized <T extends Item> T useEquipped(T item) {
    if (item != null) {
      item.useOnce();
      if (item.getUsageLeft() > 0) {
        return item;
      }
    }
    return null;
  }

  /**
   * Handles the swapping of equipped item. Use it like so: {@code thing = equipItem(thing,
   * newThing, unequippedList)}. Return {@code toBeEquipped}
   */
  private synchronized <T extends Item>
  T equipItem(T currentlyEquipped, T toBeEquipped, List<T> unequippedList) {
    Preconditions.checkNotNull(toBeEquipped);
    Preconditions.checkArgument(unequippedList.remove(toBeEquipped));
    if (currentlyEquipped != null) {
      unequippedList.add(currentlyEquipped);
    }
    return toBeEquipped;
  }
}
