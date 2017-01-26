package com.jingyuyao.tactical.model.item;

import com.google.common.base.Preconditions;

public class ItemStats {

  private final String name;
  private int usageLeft;

  public ItemStats(String name, int usageLeft) {
    Preconditions.checkArgument(usageLeft > 0);
    this.name = name;
    this.usageLeft = usageLeft;
  }

  String getName() {
    return name;
  }

  int getUsageLeft() {
    return usageLeft;
  }

  void decrementUsageLeft() {
    Preconditions.checkState(usageLeft > 0);
    usageLeft--;
  }
}
