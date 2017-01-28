package com.jingyuyao.tactical.model.item;

class WeaponData extends ItemData {

  private int attackPower;

  WeaponData() {
  }

  WeaponData(String name, int usageLeft, int attackPower) {
    super(name, usageLeft);
    this.attackPower = attackPower;
  }

  int getAttackPower() {
    return attackPower;
  }
}
