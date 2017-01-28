package com.jingyuyao.tactical.model.item;

public class GrenadeData extends WeaponData {

  private int distance;
  private int size;

  @Override
  public Item load(ItemFactory factory) {
    return factory.create(this);
  }

  int getDistance() {
    return distance;
  }

  int getSize() {
    return size;
  }
}
