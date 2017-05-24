package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * A condition that can be satisfied given a {@link ScriptEvent}. Base implementation will fail on
 * all events. Override methods to provide functionality.
 */
public abstract class Condition {

  Condition() {
  }

  /**
   * Return whether or not this condition is satisfied when {@code turn} is reached.
   */
  boolean onTurn(Turn turn, World world) {
    return false;
  }

  /**
   * Return whether or not this condition is satisfied when {@code person} dies.
   */
  boolean onShipDestroyed(Ship destroyed, World world) {
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Must stay the same for the lifetime of this condition.
   */
  @Override
  public abstract boolean equals(Object other);

  /**
   * {@inheritDoc}
   *
   * <p>Must stay the same for the lifetime of this condition.
   */
  @Override
  public abstract int hashCode();
}
