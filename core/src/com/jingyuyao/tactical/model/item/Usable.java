package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.event.RemoveItem;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
public class Usable extends Item {

  /**
   * Don't expose setter
   */
  private int usageLeft;

  Usable(EventBus eventBus, String name, int usageLeft) {
    super(eventBus, name);
    Preconditions.checkArgument(usageLeft > 0);
    this.usageLeft = usageLeft;
  }

  public int getUsageLeft() {
    return usageLeft;
  }

  /**
   * Signals this item has been used once. Fires {@link RemoveItem} when {@link #getUsageLeft()} == 0
   */
  public void useOnce() {
    Preconditions.checkState(usageLeft > 0);

    usageLeft--;
    if (usageLeft == 0) {
      post(new RemoveItem(this));
    }
  }
}
