package com.jingyuyao.tactical.model.item;

public class HealData extends ItemData {

  private final int amount;

  public HealData(String name, int usageLeft, int amount) {
    super(name, usageLeft);
    this.amount = amount;
  }

  int getAmount() {
    return amount;
  }
}
