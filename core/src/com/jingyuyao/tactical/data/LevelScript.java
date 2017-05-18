package com.jingyuyao.tactical.data;

import com.jingyuyao.tactical.model.script.LevelTrigger;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.HashMap;
import java.util.Map;

class LevelScript {

  private Map<Turn, LevelTrigger> levelTriggers = new HashMap<>();

  Map<Turn, LevelTrigger> getLevelTriggers() {
    return levelTriggers;
  }
}
