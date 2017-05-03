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
   * Damages characters in the given target. Different weapon implementations might apply different
   * amount of damage to different parts of the target. This method is responsible for whether
   * or not it respects the character's defense. (Some weapon implementation might ignore a
   * character's defense altogether)
   */
  void damages(Target target);

  ImmutableList<Target> createTargets(Movements movements, Cell from);
}
