package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.world.Cell;

public interface State {

  /**
   * Called when entering this state.
   *
   * <p>Note: Constructor is unreliable for setting up new state data since we can go back to the
   * previous state which won't be re-instantiated.
   */
  void enter();

  /**
   * Called when this state is canceled by going to previous state. Do NOT change state in this
   * method.
   */
  void canceled();

  /**
   * Called when this state exits.
   */
  void exit();

  void select(Cell cell);

  /**
   * Return the list of {@link Action} can be taken in this state.
   */
  ImmutableList<Action> getActions();
}
