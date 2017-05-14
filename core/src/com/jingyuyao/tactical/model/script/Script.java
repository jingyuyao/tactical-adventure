package com.jingyuyao.tactical.model.script;

import com.google.common.base.Optional;
import java.util.Map;

public class Script {

  private final Map<Integer, TurnScript> turnScripts;

  public Script(Map<Integer, TurnScript> turnScripts) {
    this.turnScripts = turnScripts;
  }

  public Optional<TurnScript> turnScript(int turn) {
    return Optional.fromNullable(turnScripts.get(turn));
  }
}
