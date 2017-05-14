package com.jingyuyao.tactical.model.script;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class TurnScript {

  private final List<Dialogue> dialogues;

  public TurnScript(List<Dialogue> dialogues) {
    this.dialogues = dialogues;
  }

  public ImmutableList<Dialogue> getDialogues() {
    return ImmutableList.copyOf(dialogues);
  }
}
