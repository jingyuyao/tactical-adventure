package com.jingyuyao.tactical.model.script;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.Map;

public class Script {

  private final Map<Turn, ScriptActions> turnScripts;
  private final Map<Message, ScriptActions> deathScripts;

  public Script(Map<Turn, ScriptActions> turnScripts, Map<Message, ScriptActions> deathScripts) {
    this.turnScripts = turnScripts;
    this.deathScripts = deathScripts;
  }

  public Optional<ScriptActions> turnScript(Turn turn) {
    return Optional.fromNullable(turnScripts.get(turn));
  }

  public Optional<ScriptActions> deathScript(Message name) {
    return Optional.fromNullable(deathScripts.get(name));
  }
}
