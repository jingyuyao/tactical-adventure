package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.item.event.RemoveItem;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
public class Usable extends EventBusObject implements Item {

  private final String name;
  private int usageLeft;

  Usable(EventBus eventBus, String name, int usageLeft) {
    super(eventBus);
    Preconditions.checkArgument(usageLeft > 0);
    this.name = name;
    this.usageLeft = usageLeft;
  }

  public String getName() {
    return name;
  }

  public int getUsageLeft() {
    return usageLeft;
  }

  /**
   * Signals this item has been used once. Fires {@link RemoveItem} when {@link #getUsageLeft()} ==
   * 0
   */
  public void useOnce() {
    Preconditions.checkState(usageLeft > 0);

    usageLeft--;
    if (usageLeft == 0) {
      post(new RemoveItem(this));
    }
  }
}
