package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Dialogue;
import java.util.List;

abstract class TurnScriptState extends BaseState {

  private final LevelComplete levelComplete;

  TurnScriptState(ModelBus modelBus, WorldState worldState, LevelComplete levelComplete) {
    super(modelBus, worldState);
    this.levelComplete = levelComplete;
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
      levelComplete.check(done);
    } else {
      post(new ShowDialogues(dialogues, new Promise(new Runnable() {
        @Override
        public void run() {
          levelComplete.check(done);
        }
      })));
    }
  }

  /**
   * Called when all the script stuff finished executing. Branch away here.
   */
  abstract void scriptDone();
}
