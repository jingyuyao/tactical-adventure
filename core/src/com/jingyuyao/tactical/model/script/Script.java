package com.jingyuyao.tactical.model.script;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.List;

public class Script {

  private final List<Condition> winConditions;
  private final List<Condition> loseConditions;
  private final ListMultimap<Condition, Dialogue> dialogues;
  private final Multimap<Turn, Dialogue> turnDialogues;
  private final Multimap<ResourceKey, Dialogue> deathDialogues;

  public Script(
      List<Condition> winConditions,
      List<Condition> loseConditions,
      ListMultimap<Condition, Dialogue> dialogues,
      Multimap<Turn, Dialogue> turnDialogues,
      Multimap<ResourceKey, Dialogue> deathDialogues) {
    this.winConditions = winConditions;
    this.loseConditions = loseConditions;
    this.dialogues = dialogues;
    this.turnDialogues = turnDialogues;
    this.deathDialogues = deathDialogues;
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

  public ImmutableListMultimap<Turn, Dialogue> getTurnDialogues() {
    return ImmutableListMultimap.copyOf(turnDialogues);
  }

  public ImmutableListMultimap<ResourceKey, Dialogue> getDeathDialogues() {
    return ImmutableListMultimap.copyOf(deathDialogues);
  }
}
