package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.LevelTrigger;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LevelScript {

  private Map<Turn, LevelTrigger> turnTriggers = new HashMap<>();
  private List<String> keepAlive = new ArrayList<>();

  Map<Turn, LevelTrigger> getTurnTriggers() {
    return turnTriggers;
  }

  List<String> getKeepAlive() {
    return keepAlive;
  }
}
