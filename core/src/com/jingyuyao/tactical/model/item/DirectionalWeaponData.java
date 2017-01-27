package com.jingyuyao.tactical.model.item;

public class DirectionalWeaponData extends WeaponData {

  private final int distance;

  public DirectionalWeaponData(String name, int usageLeft, int attackPower, int distance) {
    super(name, usageLeft, attackPower);
    this.distance = distance;
  }

  int getDistance() {
    return distance;
  }
}
