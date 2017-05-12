package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import javax.inject.Inject;

public class StartTurn extends BaseState {

  private final StateFactory stateFactory;

  @Inject
  StartTurn(ModelBus modelBus, WorldState worldState, StateFactory stateFactory) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
  }

  @Override
  public void enter() {
    super.enter();
    // TODO: check script for dialogue before finish...
    finish();
  }

  private void finish() {
    branchTo(stateFactory.createWaiting());
  }
}
