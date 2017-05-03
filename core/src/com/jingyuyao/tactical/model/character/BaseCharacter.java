package com.jingyuyao.tactical.model.character;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

class BaseCharacter implements Character {

  private String name;
  private int maxHp;
  private int hp;
  private int moveDistance;
  private Items items;

  BaseCharacter() {
  }

  BaseCharacter(String name, int maxHp, int hp, int moveDistance, Items items) {
    this.name = name;
    this.maxHp = maxHp;
    this.hp = hp;
    this.moveDistance = moveDistance;
    this.items = items;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getHp() {
    return hp;
  }

  @Override
  public int getMoveDistance() {
    return moveDistance;
  }

  @Override
  public int getDefense() {
    int defense = 0;
    for (Armor armor : getEquippedArmors()) {
      defense += armor.getDefense();
    }
    return defense;
  }

  @Override
  public void damageBy(int delta) {
    hp = Math.max(hp - delta, 0);
  }

  @Override
  public void healBy(int delta) {
    hp = Math.min(hp + delta, maxHp);
  }

  @Override
  public void fullHeal() {
    hp = maxHp;
  }

  @Override
  public ImmutableList<Consumable> getConsumables() {
    return items.getConsumables();
  }

  @Override
  public ImmutableList<Weapon> getWeapons() {
    return items.getWeapons();
  }

  @Override
  public ImmutableList<Armor> getEquippedArmors() {
    return items.getEquippedArmors();
  }

  @Override
  public ImmutableList<Armor> getUnequippedArmors() {
    return items.getUnequippedArmors();
  }

  @Override
  public void useConsumable(Consumable consumable) {
    items.useConsumable(consumable);
  }

  @Override
  public void useWeapon(Weapon weapon) {
    items.useWeapon(weapon);
  }

  @Override
  public void useEquippedArmors() {
    items.useEquippedArmors();
  }

  @Override
  public void equipBodyArmor(Armor armor) {
    items.equipBodyArmor(armor);
  }
}
