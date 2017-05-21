package com.jingyuyao.tactical.model.ship;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ModelBundle;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;

public class Ship {

  private String name;
  private AutoPilot autoPilot = new NoAutoPilot();
  private Stats stats = new Stats();
  private Cockpit cockpit = new Cockpit();
  private Items items = new Items();

  Ship() {
  }

  Ship(String name, AutoPilot autoPilot, Stats stats, Cockpit cockpit, Items items) {
    this.name = name;
    this.autoPilot = autoPilot;
    this.stats = stats;
    this.cockpit = cockpit;
    this.items = items;
  }

  /**
   * The animation for this ship. Based on ship's name.
   */
  public ResourceKey getAnimation() {
    return ModelBundle.SHIP_ANIMATIONS.get(name);
  }

  /**
   * The name of this ship. May not be unique.
   */
  public ResourceKey getName() {
    return ModelBundle.SHIP_NAME.get(name);
  }

  public PilotResponse getAutoPilotResponse(World world, Cell cell) {
    Preconditions.checkArgument(cell.ship().isPresent());
    Preconditions.checkArgument(cell.ship().get().equals(this));
    return autoPilot.getResponse(world, cell);
  }

  /**
   * Return whether or not this ship can currently be controlled by the player.
   */
  public boolean isControllable() {
    return stats.isControllable();
  }

  public void setControllable(boolean controllable) {
    stats.setControllable(controllable);
  }

  /**
   * Return whether this ship is in {@code group}.
   */
  public boolean inGroup(ShipGroup group) {
    return stats.inGroup(group);
  }

  public int getHp() {
    return stats.getHp();
  }

  public int getMoveDistance() {
    return stats.getMoveDistance();
  }

  public int getDefense() {
    return items.getDefense();
  }

  public void damageBy(int delta) {
    stats.damageBy(delta);
  }

  public void healBy(int delta) {
    stats.healBy(delta);
  }

  public ImmutableList<Person> getCrew() {
    return ImmutableList.<Person>copyOf(cockpit.getPilots());
  }

  public ImmutableList<Consumable> getConsumables() {
    return items.getConsumables();
  }

  public ImmutableList<Weapon> getWeapons() {
    return items.getWeapons();
  }

  public ImmutableList<Armor> getEquippedArmors() {
    return items.getEquippedArmors();
  }

  public ImmutableList<Armor> getStashedArmors() {
    return items.getStashedArmors();
  }

  public void useConsumable(Consumable consumable) {
    items.useConsumable(consumable);
  }

  public void useWeapon(Weapon weapon) {
    items.useWeapon(weapon);
  }

  public void useEquippedArmors() {
    items.useEquippedArmors();
  }

  /**
   * Equips {@code armor} from the stash. Replaces the previously equipped armor of the same type if
   * its present.
   */
  public void equipArmor(Armor armor) {
    items.equipArmor(armor);
  }

  /**
   * Unequips equipped {@code armor} and move it to the stash.
   */
  public void unequipArmor(Armor armor) {
    items.unequipArmor(armor);
  }
}
