package com.jingyuyao.tactical.model.script;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Script implements Serializable {

  private List<Condition> winConditions;
  private List<Condition> loseConditions;
  private Map<Condition, List<Dialogue>> dialogues;
  private Map<Condition, ActivateGroup> groupActivations;
  private Map<Condition, DeactivateGroup> groupDeactivations;

  Script() {
  }

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
