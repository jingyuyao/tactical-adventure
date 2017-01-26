package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Character;

/**
 * An {@link Item} that can be used be used on a {@link Character}.
 */
public interface Consumable extends Item {

  /**
   * Apply this {@link Item}'s effects to its {@link Item#getOwner()}.
   */
  void consume();
}
