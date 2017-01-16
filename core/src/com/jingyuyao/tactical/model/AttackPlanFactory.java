package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.character.Character;
import javax.inject.Singleton;

@Singleton
public class AttackPlanFactory {

  public AttackPlan create(Character attacker, Character defender) {
    return new AttackPlan(attacker, defender);
  }
}
