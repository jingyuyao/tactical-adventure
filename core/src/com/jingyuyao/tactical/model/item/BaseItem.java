package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.common.EventBusObject;
import com.jingyuyao.tactical.model.item.event.RemoveItem;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
public class BaseItem extends EventBusObject implements Item {

  private final String name;
  private int usageLeft;

  BaseItem(EventBus eventBus, String name, int usageLeft) {
    super(eventBus);
    Preconditions.checkArgument(usageLeft > 0);
    this.name = name;
    this.usageLeft = usageLeft;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getUsageLeft() {
    return usageLeft;
  }

  /**
   * Call to use this item once.
   * <br>
   * Invariant: {@link com.jingyuyao.tactical.model.item.event.RemoveItem} must be posted
   * if {@link #getUsageLeft()} == 0 after the usage
   */
  void useOnce() {
    Preconditions.checkState(usageLeft > 0);

    usageLeft--;
    if (usageLeft == 0) {
      post(new RemoveItem(this));
    }
  }
}
