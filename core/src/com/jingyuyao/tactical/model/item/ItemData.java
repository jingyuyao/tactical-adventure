package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;

public abstract class ItemData {

  private String name;
  private int usageLeft;

  String getName() {
    return name;
  }

  int getUsageLeft() {
    return usageLeft;
  }

  void useOnce() {
    Preconditions.checkState(usageLeft > 0);
    usageLeft--;
  }

  /**
   * Enables the visitor pattern for item creation.
   */
  public abstract Item load(ItemFactory factory);
}
