package com.jingyuyao.tactical.model;

import com.google.common.base.MoreObjects;
import com.jingyuyao.tactical.model.character.Character;

public class AttackPlan {

  private final Character attacker;
  private final Character defender;

  AttackPlan(Character attacker, Character defender) {
    this.attacker = attacker;
    this.defender = defender;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("attacker", attacker.toString())
        .add("defender", defender.toString())
        .toString();
  }
}
