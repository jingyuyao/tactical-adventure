package com.jingyuyao.tactical.model.item;

public class WeaponData extends ItemData {

  private final int attackPower;

  public WeaponData(String name, int usageLeft, int attackPower) {
    super(name, usageLeft);
    this.attackPower = attackPower;
  }

  int getAttackPower() {
    return attackPower;
  }
}
