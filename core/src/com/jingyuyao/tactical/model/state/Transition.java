package com.jingyuyao.tactical.model.state;

import javax.inject.Inject;

/**
 * A {@link State} that doesn't do anything. Useful as an intermediate state to wait for
 * animation to finish. The original caller is responsible for switching out of this {@link State}.
 */
class Transition extends BaseState {

  @Inject
  Transition(MapState mapState) {
    super(mapState);
  }

  @Override
  public void exit() {
    // This state is temporary, don't keep it on the state stack
    popLast();
  }
}
