package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.character.Ship;

/**
 * An {@link Item} that can be used be used on a {@link Ship}.
 */
public interface Consumable extends Item {

  /**
   * Apply this {@link Item}'s effects to {@code ship}.
   */
  void apply(Ship ship);
}
