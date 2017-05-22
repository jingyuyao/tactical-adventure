package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.GroupActivation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LevelScript {

  private List<Condition> loseConditions = new ArrayList<>();
  private List<Condition> winConditions = new ArrayList<>();
  private Map<Condition, GroupActivation> groupActivations = new HashMap<>();

  private LevelScript() {
  }

  List<Condition> getLoseConditions() {
    return loseConditions;
  }

  List<Condition> getWinConditions() {
    return winConditions;
  }

  Map<Condition, GroupActivation> getGroupActivations() {
    return groupActivations;
  }
}
