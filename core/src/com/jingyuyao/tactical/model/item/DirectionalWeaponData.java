package com.jingyuyao.tactical.model.item;

public class DirectionalWeaponData extends WeaponData {

  private int distance;

  @Override
  public Item load(ItemFactory factory) {
    return factory.create(this);
  }

  int getDistance() {
    return distance;
  }
}
