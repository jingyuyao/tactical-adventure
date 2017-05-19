package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import java.util.List;

abstract class TurnScriptState extends BaseState {

  private final ScriptRunner scriptRunner;

  TurnScriptState(ModelBus modelBus, WorldState worldState, ScriptRunner scriptRunner) {
    super(modelBus, worldState);
    this.scriptRunner = scriptRunner;
  }

  @Override
  public void enter() {
    super.enter();
    processTurn(new Runnable() {
      @Override
      public void run() {
        scriptDone();
      }
    });
  }

  private void processTurn(final Runnable done) {
    List<Dialogue> dialogues = getScript().getTurnDialogues().get(getTurn());
    if (dialogues.isEmpty()) {
      scriptRunner.check(done);
    } else {
      post(new ShowDialogues(dialogues, new Promise(new Runnable() {
        @Override
        public void run() {
          scriptRunner.check(done);
        }
      })));
    }
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void scriptDone();
}
