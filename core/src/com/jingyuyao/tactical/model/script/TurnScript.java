package com.jingyuyao.tactical.model.script;

public class TurnScript {

  private final ScriptActions start;
  private final ScriptActions end;

  public TurnScript(ScriptActions start, ScriptActions end) {
    this.start = start;
    this.end = end;
  }

  public ScriptActions getStart() {
    return start;
  }

  public ScriptActions getEnd() {
    return end;
  }
}
