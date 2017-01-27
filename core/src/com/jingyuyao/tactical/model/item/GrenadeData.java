package com.jingyuyao.tactical.model.item;

public class GrenadeData extends WeaponData {

  private final int distance;
  private final int size;

  public GrenadeData(String name, int usageLeft, int attackPower, int distance, int size) {
    super(name, usageLeft, attackPower);
    this.distance = distance;
    this.size = size;
  }

  int getDistance() {
    return distance;
  }

  int getSize() {
    return size;
  }
}
