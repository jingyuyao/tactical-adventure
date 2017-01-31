package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem implements Item {

  private String name;
  private int usageLeft;

  BaseItem() {
  }

  BaseItem(String name, int usageLeft) {
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

  @Override
  public void useOnce() {
    Preconditions.checkState(usageLeft > 0);
    usageLeft--;
  }
}
