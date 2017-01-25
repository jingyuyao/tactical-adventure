package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.character.Character;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem<T extends ItemStats> implements Item {

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
   * <br>
   * Invariant: {@link com.jingyuyao.tactical.model.item.event.RemoveItem} must be posted
   * if {@link #getUsageLeft()} == 0 after the usage
   */
  void useOnce() {
    Preconditions.checkState(itemStats.getUsageLeft() > 0);

    itemStats.setUsageLeft(itemStats.getUsageLeft() - 1);
    if (itemStats.getUsageLeft() == 0) {
      owner.removeItem(this);
    }
  }
}
