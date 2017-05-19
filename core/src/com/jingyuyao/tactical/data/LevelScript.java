package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Condition;
import java.util.ArrayList;
import java.util.List;

class LevelScript {

  private List<Condition> loseConditions = new ArrayList<>();
  private List<Condition> winConditions = new ArrayList<>();

  List<Condition> getLoseConditions() {
    return loseConditions;
  }

  List<Condition> getWinConditions() {
    return winConditions;
  }
}
