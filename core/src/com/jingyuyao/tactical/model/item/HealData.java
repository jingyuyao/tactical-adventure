package com.jingyuyao.tactical.model.item;

public class HealData extends ItemData {

  private int amount;

  @Override
  public Item load(ItemFactory factory) {
    return factory.create(this);
  }

  int getAmount() {
    return amount;
  }
}
