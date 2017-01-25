package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.state.State;

/**
 * Event fired when {@link State} has changed.
 */
public class StateChanged extends ObjectEvent<State> {

  public StateChanged(State newState) {
    super(newState);
  }
}
