package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.target.Target;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public abstract class Weapon extends BaseItem {

  private final int attackPower;

  Weapon(
      EventBus eventBus,
      @Assisted String name,
      @Assisted("usageLeft") int usageLeft,
      @Assisted("attackPower") int attackPower) {
    super(eventBus, name, usageLeft);
    this.attackPower = attackPower;
  }

  public int getAttackPower() {
    return attackPower;
  }

  public abstract ImmutableList<Target> createTargets(Character attacker);
}
