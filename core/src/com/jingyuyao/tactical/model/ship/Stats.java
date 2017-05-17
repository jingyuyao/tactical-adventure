package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.Allegiance;

class Stats {

  private Allegiance allegiance = Allegiance.ENEMY;
  private boolean controllable;
  private int maxHp;
  private int hp;
  private int moveDistance;

  Stats() {
  }

  Stats(int maxHp, int hp, int moveDistance) {
    this.maxHp = maxHp;
    this.hp = hp;
    this.moveDistance = moveDistance;
  }

  Allegiance getAllegiance() {
    return allegiance;
  }

  boolean isControllable() {
    return controllable;
  }

  void setControllable(boolean controllable) {
    this.controllable = controllable;
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

  void fullHeal() {
    hp = maxHp;
  }
}
