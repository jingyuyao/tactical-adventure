package com.jingyuyao.tactical.model.ship;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;

/**
 * A {@link Ship} that can't be controlled.
 */
class BaseShip implements Ship {

  private String nameKey;
  private String resourceKey;
  private Stats stats;  // required
  private Cockpit cockpit = new Cockpit();
  private Items items = new Items();

  BaseShip() {
  }

  BaseShip(String nameKey, String resourceKey, Stats stats, Cockpit cockpit, Items items) {
    this.nameKey = nameKey;
    this.resourceKey = resourceKey;
    this.stats = stats;
    this.cockpit = cockpit;
    this.items = items;
  }

  @Override
  public boolean canControl() {
    return false;
  }

  @Override
  public String getResourceKey() {
    return resourceKey;
  }

  @Override
  public ResourceKey getName() {
    return ModelBundle.SHIP_NAME.get(nameKey);
  }

  @Override
  public int getHp() {
    return stats.getHp();
  }

  @Override
  public int getMoveDistance() {
    return stats.getMoveDistance();
  }

  @Override
  public int getDefense() {
    return items.getDefense();
  }

  @Override
  public void damageBy(int delta) {
    stats.damageBy(delta);
  }

  @Override
  public void healBy(int delta) {
    stats.healBy(delta);
  }

  @Override
  public void fullHeal() {
    stats.fullHeal();
  }

  @Override
  public ImmutableList<Person> getPilots() {
    return cockpit.getPilots();
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
  public ImmutableList<Armor> getStashedArmors() {
    return items.getStashedArmors();
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
  public void equipArmor(Armor armor) {
    items.equipArmor(armor);
  }

  @Override
  public void unequipArmor(Armor armor) {
    items.unequipArmor(armor);
  }
}
