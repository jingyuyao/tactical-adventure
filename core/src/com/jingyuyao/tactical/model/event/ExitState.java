package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.state.State;

public class ExitState extends ObjectEvent<State> {

  public ExitState(State object) {
    super(object);
  }
}
