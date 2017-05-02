package com.jingyuyao.tactical.model.character;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.List;

/**
 * Internal representation of all the {@link Item} a {@link Character} holds.
 * <br/>
 * Invariants:
 * <br/>
 * - An item can either be in unequippedItems or in one of the other fields but not both
 * at the same time
 */
class Items {

  private List<Item> unequippedItems;
  private Armor bodyArmor;
  private Weapon weapon1;
  private Weapon weapon2;

  Items(List<Item> unequippedItems, Armor bodyArmor, Weapon weapon1, Weapon weapon2) {
    this.unequippedItems = unequippedItems;
    this.bodyArmor = bodyArmor;
    this.weapon1 = weapon1;
    this.weapon2 = weapon2;
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

  ImmutableList<Item> getUnequippedItems() {
    return ImmutableList.copyOf(unequippedItems);
  }

  ImmutableList<Armor> getEquippedArmors() {
    return ignoreNullOf(bodyArmor);
  }

  ImmutableList<Weapon> getEquippedWeapons() {
    return ignoreNullOf(weapon1, weapon2);
  }

  void useArmors() {
    bodyArmor = useItem(bodyArmor);
  }

  void useWeapons() {
    weapon1 = useItem(weapon1);
    weapon2 = useItem(weapon2);
  }

  void equipBodyArmor(Armor armor) {
    bodyArmor = equipItem(bodyArmor, armor);
  }

  void equipWeapon1(Weapon weapon) {
    weapon1 = equipItem(weapon1, weapon);
  }

  void equipWeapon2(Weapon weapon) {
    weapon2 = equipItem(weapon2, weapon);
  }

  /**
   * Use the given item if its not null. Return the item if it still has usage left, null otherwise.
   */
  private synchronized <T extends Item> T useItem(T item) {
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
   * newThing)}. Return {@code toBeEquipped}
   */
  private synchronized <T extends Item> T equipItem(T currentlyEquipped, T toBeEquipped) {
    Preconditions.checkNotNull(toBeEquipped);
    Preconditions.checkArgument(unequippedItems.remove(toBeEquipped));
    if (currentlyEquipped != null) {
      unequippedItems.add(currentlyEquipped);
    }
    return toBeEquipped;
  }
}
