package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.script.Dialogue;
import java.util.List;

public class ShowDialogues {

  private final List<Dialogue> dialogues;
  private final Promise promise;

  public ShowDialogues(List<Dialogue> dialogues, Promise promise) {
    this.dialogues = dialogues;
    this.promise = promise;
  }

  public List<Dialogue> getDialogues() {
    return dialogues;
  }

  public void complete() {
    promise.complete();
  }
}
