package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.battle.TargetFactory;
import com.jingyuyao.tactical.model.character.Character;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public abstract class Weapon extends Usable {

  private final int attackPower;
  private final TargetFactory targetFactory;

  Weapon(
      EventBus eventBus, String name, int usageLeft, int attackPower, TargetFactory targetFactory) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
    this.targetFactory = targetFactory;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public ImmutableList<Target> createTargets(Character attacker) {
    return targetFactory.create(attacker, this);
  }
}
