package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.event.AbstractEvent;
import com.jingyuyao.tactical.model.state.State;

/**
 * Event fired when {@link State} has changed.
 */
public class StateChanged extends AbstractEvent<State> {

  public StateChanged(State newState) {
    super(newState);
  }
}
