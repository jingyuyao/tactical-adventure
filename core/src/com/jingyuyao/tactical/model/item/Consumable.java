package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.ship.Ship;

/**
 * An {@link Item} that can be used be used on a {@link Ship}.
 */
public class Consumable extends Item {

  Consumable(String name, int usageLeft) {
    super(name, usageLeft);
  }

  /**
   * Apply this {@link Item}'s effects to {@code ship}.
   */
  public void apply(Ship ship) {
  }
}
