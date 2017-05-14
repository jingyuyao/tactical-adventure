package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.script.TurnScript;

abstract class ScriptState extends BaseState {

  ScriptState(ModelBus modelBus, WorldState worldState) {
    super(modelBus, worldState);
  }

  @Override
  public void enter() {
    super.enter();
    Optional<TurnScript> turnScriptOpt = currentTurnScript();
    if (turnScriptOpt.isPresent()) {
      showDialogue(getScriptActions(turnScriptOpt.get()));
    } else {
      finish();
    }
  }

  private void showDialogue(ScriptActions scriptActions) {
    // TODO: check if the character is in the world
    if (scriptActions.getDialogues().isEmpty()) {
      finish();
    } else {
      post(new ShowDialogues(scriptActions.getDialogues(), new Promise(new Runnable() {
        @Override
        public void run() {
          finish();
        }
      })));
    }
  }

  /**
   * Function to get the {@link ScriptActions} from the current {@link TurnScript}
   */
  abstract ScriptActions getScriptActions(TurnScript turnScript);

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void finish();
}
