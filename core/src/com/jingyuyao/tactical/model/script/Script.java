package com.jingyuyao.tactical.model.script;

import java.util.List;
import java.util.Map;

public class Script {

  private final List<Condition> winConditions;
  private final List<Condition> loseConditions;
  private final Map<Condition, List<Dialogue>> dialogues;
  private final Map<Condition, ActivateGroup> groupActivations;
  private final Map<Condition, DeactivateGroup> groupDeactivations;

  public Script(
      List<Condition> winConditions,
      List<Condition> loseConditions,
      Map<Condition, List<Dialogue>> dialogues,
      Map<Condition, ActivateGroup> groupActivations,
      Map<Condition, DeactivateGroup> groupDeactivations) {
    this.winConditions = winConditions;
    this.loseConditions = loseConditions;
    this.dialogues = dialogues;
    this.groupActivations = groupActivations;
    this.groupDeactivations = groupDeactivations;
  }

  public List<Condition> getWinConditions() {
    return winConditions;
  }

  public List<Condition> getLoseConditions() {
    return loseConditions;
  }

  public Map<Condition, List<Dialogue>> getDialogues() {
    return dialogues;
  }

  public Map<Condition, ActivateGroup> getGroupActivations() {
    return groupActivations;
  }

  public Map<Condition, DeactivateGroup> getGroupDeactivations() {
    return groupDeactivations;
  }
}
