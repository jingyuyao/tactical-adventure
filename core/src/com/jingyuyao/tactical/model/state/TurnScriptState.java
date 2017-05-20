package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;

abstract class TurnScriptState extends BaseState {

  private final ScriptRunner scriptRunner;
  private boolean triggered = false;

  TurnScriptState(ModelBus modelBus, WorldState worldState, ScriptRunner scriptRunner) {
    super(modelBus, worldState);
    this.scriptRunner = scriptRunner;
  }

  @Override
  public void enter() {
    super.enter();
    // enter() could be called again if the next state cancels, but we don't want to trigger
    // the same script twice.
    if (!triggered) {
      triggered = true;
      scriptRunner.triggerTurn(new Runnable() {
        @Override
        public void run() {
          scriptDone();
        }
      });
    }
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void scriptDone();
}
