package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.script.TurnScript;
import javax.inject.Inject;

public class StartTurn extends ScriptState {

  private final StateFactory stateFactory;

  @Inject
  StartTurn(ModelBus modelBus, WorldState worldState, StateFactory stateFactory) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
  }

  @Override
  ScriptActions getScriptActions(TurnScript turnScript) {
    return turnScript.getStart();
  }

  @Override
  void finish() {
    branchTo(stateFactory.createWaiting());
  }
}
