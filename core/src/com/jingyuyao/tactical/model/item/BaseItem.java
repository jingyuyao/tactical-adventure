package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem<T extends ItemData> implements Item {

  private final Character owner;
  private final T itemStats;

  BaseItem(Character owner, T itemStats) {
    this.owner = owner;
    this.itemStats = itemStats;
  }

  @Override
  public Character getOwner() {
    return owner;
  }

  @Override
  public String getName() {
    return itemStats.getName();
  }

  @Override
  public int getUsageLeft() {
    return itemStats.getUsageLeft();
  }

  T getItemStats() {
    return itemStats;
  }

  /**
   * Call to use this item once.
   */
  void useOnce() {
    itemStats.decrementUsageLeft();

    if (itemStats.getUsageLeft() == 0) {
      owner.removeItem(this);
    }
  }
}
