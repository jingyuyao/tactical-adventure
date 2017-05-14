package com.jingyuyao.tactical.model.script;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.Map;

public class Script {

  private final Map<Turn, TurnScript> turnScripts;

  public Script(Map<Turn, TurnScript> turnScripts) {
    this.turnScripts = turnScripts;
  }

  public Optional<TurnScript> turnScript(Turn turn) {
    return Optional.fromNullable(turnScripts.get(turn));
  }
}
