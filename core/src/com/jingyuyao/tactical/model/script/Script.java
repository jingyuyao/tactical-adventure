package com.jingyuyao.tactical.model.script;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.List;

public class Script {

  private final List<Condition> winConditions;
  private final List<Condition> loseConditions;
  private final ListMultimap<Condition, Dialogue> dialogues;

  public Script(
      List<Condition> winConditions,
      List<Condition> loseConditions,
      ListMultimap<Condition, Dialogue> dialogues) {
    this.winConditions = winConditions;
    this.loseConditions = loseConditions;
    this.dialogues = dialogues;
  }

  public ImmutableList<Condition> getWinConditions() {
    return ImmutableList.copyOf(winConditions);
  }

  public ImmutableList<Condition> getLoseConditions() {
    return ImmutableList.copyOf(loseConditions);
  }

  public ImmutableListMultimap<Condition, Dialogue> getDialogues() {
    return ImmutableListMultimap.copyOf(dialogues);
  }
}
