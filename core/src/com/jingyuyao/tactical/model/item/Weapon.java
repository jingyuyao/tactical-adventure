package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.map.Cell;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status. Not a {@link Consumable}
 * since the effect of a {@link Weapon} depends on its user.
 */
public interface Weapon extends Item {

  int getAttackPower();

  void damages(Target target);

  ImmutableList<Target> createTargets(Cell from);
}
