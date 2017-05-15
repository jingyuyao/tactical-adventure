package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.script.ScriptActions;

abstract class ScriptState extends BaseState {

  ScriptState(ModelBus modelBus, WorldState worldState) {
    super(modelBus, worldState);
  }

  @Override
  public void enter() {
    super.enter();
    Optional<ScriptActions> scriptActionsOpt = currentTurnScript();
    if (scriptActionsOpt.isPresent()) {
      scriptActionsOpt.get().execute(getModelBus(), new Runnable() {
        @Override
        public void run() {
          finish();
        }
      });
    } else {
      finish();
    }
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void finish();
}
