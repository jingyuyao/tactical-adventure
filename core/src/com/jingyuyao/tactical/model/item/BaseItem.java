package com.jingyuyao.tactical.model.item;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem<T extends ItemData> implements Item {

  private final T data;

  BaseItem(T data) {
    this.data = data;
  }

  @Override
  public String getName() {
    return data.getName();
  }

  @Override
  public int getUsageLeft() {
    return data.getUsageLeft();
  }

  @Override
  public void useOnce() {
    data.useOnce();
  }

  T getData() {
    return data;
  }
}
