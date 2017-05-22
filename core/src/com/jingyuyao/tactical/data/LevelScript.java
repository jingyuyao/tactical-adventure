package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.ActivateGroup;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.DeactivateGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LevelScript {

  private List<Condition> loseConditions = new ArrayList<>();
  private List<Condition> winConditions = new ArrayList<>();
  private Map<Condition, ActivateGroup> groupActivations = new HashMap<>();
  private Map<Condition, DeactivateGroup> groupDeactivations = new HashMap<>();

  private LevelScript() {
  }

  List<Condition> getLoseConditions() {
    return loseConditions;
  }

  List<Condition> getWinConditions() {
    return winConditions;
  }

  Map<Condition, ActivateGroup> getGroupActivations() {
    return groupActivations;
  }

  Map<Condition, DeactivateGroup> getGroupDeactivations() {
    return groupDeactivations;
  }
}
