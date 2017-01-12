package com.jingyuyao.tactical.model;

import com.google.common.base.MoreObjects;
import com.jingyuyao.tactical.model.character.Character;

// TODO: this should now be an event
public class AttackPlan {

  private final Character attacker;
  private final Character defender;

  AttackPlan(Character attacker, Character defender) {
    this.attacker = attacker;
    this.defender = defender;
  }

  /**
   * Executes this attack plan.
   */
  // TODO: we don't need this anymore, move it to the Action
  public void execute() {
    attacker.tryHit(defender);
    // no need to check for health as tryHit() will handle what happens when a dead character
    // tries to attack
    defender.tryHit(attacker);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("attacker", attacker.toString())
        .add("defender", defender.toString())
        .toString();
  }
}
