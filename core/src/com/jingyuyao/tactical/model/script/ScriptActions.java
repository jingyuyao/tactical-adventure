package com.jingyuyao.tactical.model.script;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import java.util.List;

public class ScriptActions {

  private final List<Dialogue> dialogues;

  public ScriptActions(List<Dialogue> dialogues) {
    this.dialogues = dialogues;
  }

  public ImmutableList<Dialogue> getDialogues() {
    return ImmutableList.copyOf(dialogues);
  }

  public void execute(ModelBus modelBus, Runnable done) {
    // TODO: check if the character is in the world
    if (dialogues.isEmpty()) {
      done.run();
    } else {
      modelBus.post(new ShowDialogues(dialogues, new Promise(done)));
    }
  }
}
