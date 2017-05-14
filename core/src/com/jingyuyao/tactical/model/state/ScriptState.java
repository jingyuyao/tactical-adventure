package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.ScriptActions;
import java.util.List;

abstract class ScriptState extends BaseState {

  ScriptState(ModelBus modelBus, WorldState worldState) {
    super(modelBus, worldState);
  }

  @Override
  public void enter() {
    super.enter();
    Optional<ScriptActions> turnScriptOpt = currentTurnScript();
    if (turnScriptOpt.isPresent()) {
      showDialogue(turnScriptOpt.get());
    } else {
      finish();
    }
  }

  private void showDialogue(ScriptActions scriptActions) {
    // TODO: check if the character is in the world
    List<Dialogue> dialogues = scriptActions.getDialogues();
    if (dialogues.isEmpty()) {
      finish();
    } else {
      post(new ShowDialogues(dialogues, new Promise(new Runnable() {
        @Override
        public void run() {
          finish();
        }
      })));
    }
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void finish();
}
