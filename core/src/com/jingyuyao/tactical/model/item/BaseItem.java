package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.event.RemoveItem;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem<T extends ItemStats> implements Item {

  private final EventBus eventBus;
  private final T itemStats;

  BaseItem(EventBus eventBus, T itemStats) {
    this.eventBus = eventBus;
    this.itemStats = itemStats;
  }

  @Override
  public String getName() {
    return itemStats.getName();
  }

  @Override
  public int getUsageLeft() {
    return itemStats.getUsageLeft();
  }

  EventBus getEventBus() {
    return eventBus;
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
      eventBus.post(new RemoveItem(this));
    }
  }
}
