package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.Condition;
import java.util.ArrayList;
import java.util.List;

class GameScript {

  private List<Condition> loseConditions = new ArrayList<>();

  List<Condition> getLoseConditions() {
    return loseConditions;
  }
}
