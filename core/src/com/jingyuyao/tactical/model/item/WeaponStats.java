package com.jingyuyao.tactical.model.item;

public class WeaponStats extends ItemStats {

  private final int attackPower;

  public WeaponStats(String name, int usageLeft, int attackPower) {
    super(name, usageLeft);
    this.attackPower = attackPower;
  }

  int getAttackPower() {
    return attackPower;
  }
}
