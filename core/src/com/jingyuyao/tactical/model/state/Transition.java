package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import javax.inject.Inject;

/**
 * A {@link State} that doesn't do anything. Useful as an intermediate state to wait for
 * animation to finish. The original caller is responsible for switching out of this {@link State}.
 */
public class Transition extends BaseState {

  @Inject
  Transition(@ModelEventBus EventBus eventBus, MapState mapState) {
    super(eventBus, mapState);
  }

  @Override
  public void exit() {
    super.exit();
    // This state is temporary, don't keep it on the state stack
    popLast();
  }
}
