package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

public interface Condition {

  /**
   * Return whether this {@link Condition} as been met given the world and world state.
   */
  boolean isMet(Turn turn, World world);

  /**
   * Return whether this {@link Condition} has been triggered or not.
   */
  boolean isTriggered();

  /**
   * Mark this condition as triggered.
   */
  void triggered();
}
