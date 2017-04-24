package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import javax.inject.Inject;

/**
 * A {@link State} that doesn't do anything. Useful as an intermediate state to wait for
 * animation to finish. The original caller is responsible for switching out of this {@link State}.
 */
public class Transition extends BaseState {

  @Inject
  Transition(ModelBus modelBus, WorldState worldState) {
    super(modelBus, worldState);
  }

  @Override
  public void exit() {
    super.exit();
    // This state is temporary, don't keep it on the state stack
    removeSelf();
  }
}
