package com.jingyuyao.tactical.model.ship;

class Stats {

  private int maxHp;
  private int hp;
  private int moveDistance;

  private Stats() {
  }

  Stats(int maxHp, int hp, int moveDistance) {
    this.maxHp = maxHp;
    this.hp = hp;
    this.moveDistance = moveDistance;
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
