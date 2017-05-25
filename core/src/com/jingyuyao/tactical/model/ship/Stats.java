package com.jingyuyao.tactical.model.ship;

import java.io.Serializable;
import java.util.Set;

class Stats implements Serializable {

  private Set<ShipGroup> groups;
  private boolean controllable;
  private int maxHp;
  private int hp;
  private int moveDistance;

  Stats() {
  }

  Stats(Set<ShipGroup> groups, int maxHp, int hp, int moveDistance) {
    this.groups = groups;
    this.maxHp = maxHp;
    this.hp = hp;
    this.moveDistance = moveDistance;
  }

  boolean isControllable() {
    return controllable;
  }

  void setControllable(boolean controllable) {
    this.controllable = controllable;
  }

  boolean inGroup(ShipGroup group) {
    return groups.contains(group);
  }

  int getHp() {
    return hp;
  }

  int getMoveDistance() {
    return moveDistance;
  }

  void damageBy(int delta) {
    hp = Math.max(hp - delta, 0);
  }

  void healBy(int delta) {
    hp = Math.min(hp + delta, maxHp);
  }
}
