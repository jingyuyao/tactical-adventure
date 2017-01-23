package com.jingyuyao.tactical.model.item;

public class DirectionalWeaponStats extends WeaponStats {

  private final int distance;

  public DirectionalWeaponStats(String name, int usageLeft, int attackPower, int distance) {
    super(name, usageLeft, attackPower);
    this.distance = distance;
  }

  int getDistance() {
    return distance;
  }
}
