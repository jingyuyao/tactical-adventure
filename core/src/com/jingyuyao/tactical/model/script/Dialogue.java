package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.resource.StringKey;

public class Dialogue {

  private final StringKey name;
  private final StringKey text;

  public Dialogue(StringKey name, StringKey text) {
    this.name = name;
    this.text = text;
  }

  public StringKey getName() {
    return name;
  }

  public StringKey getText() {
    return text;
  }
}
