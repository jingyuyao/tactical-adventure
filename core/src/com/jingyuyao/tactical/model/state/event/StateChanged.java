package com.jingyuyao.tactical.model.state.event;

import com.jingyuyao.tactical.model.event.ObjectEvent;
import com.jingyuyao.tactical.model.state.State;

/**
 * Event fired when {@link State} has changed.
 */
public class StateChanged extends ObjectEvent<State> {

  public StateChanged(State newState) {
    super(newState);
  }
}
