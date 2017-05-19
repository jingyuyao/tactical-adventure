package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;

abstract class TurnScriptState extends BaseState {

  private final ScriptRunner scriptRunner;

  TurnScriptState(ModelBus modelBus, WorldState worldState, ScriptRunner scriptRunner) {
    super(modelBus, worldState);
    this.scriptRunner = scriptRunner;
  }

  @Override
  public void enter() {
    super.enter();
    scriptRunner.triggerTurn(new Runnable() {
      @Override
      public void run() {
        scriptDone();
      }
    });
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void scriptDone();
}
