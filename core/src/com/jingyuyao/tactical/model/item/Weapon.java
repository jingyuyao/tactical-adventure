package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public interface Weapon extends Item {

  /**
   * Apply this weapon's effect to target. Different implementations can do completely different
   * things.
   */
  void apply(Character attacker, Target target);

  ImmutableList<Target> createTargets(Movements movements, Cell from);
}
