package com.jingyuyao.tactical.model.script;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import java.util.List;

public class ScriptActions {

  private final List<Dialogue> dialogues;
  private final LevelTrigger levelTrigger;

  public ScriptActions(List<Dialogue> dialogues, LevelTrigger levelTrigger) {
    this.dialogues = dialogues;
    this.levelTrigger = levelTrigger;
  }

  public ImmutableList<Dialogue> getDialogues() {
    return ImmutableList.copyOf(dialogues);
  }

  public LevelTrigger getLevelTrigger() {
    return levelTrigger;
  }

  public void execute(final ModelBus modelBus, final Runnable done) {
    processDialogues(modelBus).done(new Runnable() {
      @Override
      public void run() {
        processTrigger(modelBus);
        done.run();
      }
    });
  }

  private Promise processDialogues(ModelBus modelBus) {
    // TODO: check if the person is in the world
    if (dialogues.isEmpty()) {
      return Promise.immediate();
    }
    Promise promise = new Promise();
    modelBus.post(new ShowDialogues(dialogues, promise));
    return promise;
  }

  private void processTrigger(ModelBus modelBus) {
    switch (levelTrigger) {
      case WON:
        modelBus.post(new LevelWon());
        break;
      case LOST:
        modelBus.post(new LevelLost());
        break;
    }
  }
}
