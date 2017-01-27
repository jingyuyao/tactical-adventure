package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;

/**
 * An {@link Item} that can be used and has a limited number of usages.
 */
class BaseItem<T extends ItemData> implements Item {

  private final Character owner;
  private final T data;

  BaseItem(Character owner, T data) {
    this.owner = owner;
    this.data = data;
  }

  @Override
  public Character getOwner() {
    return owner;
  }

  @Override
  public String getName() {
    return data.getName();
  }

  @Override
  public int getUsageLeft() {
    return data.getUsageLeft();
  }

  T getData() {
    return data;
  }

  /**
   * Call to use this item once.
   */
  void useOnce() {
    data.decrementUsageLeft();

    if (data.getUsageLeft() == 0) {
      owner.removeItem(this);
    }
  }
}
