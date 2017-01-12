package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Singleton;

@Singleton
public class AttackPlanFactory {

  // TODO: do we need the factory?
  public AttackPlan create(Character attacker, Character defender) {
    return new AttackPlan(attacker, defender);
  }
}
