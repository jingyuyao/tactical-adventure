package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.resource.StringKey;
import java.io.Serializable;

public class Dialogue implements Serializable {

  private StringKey name;
  private StringKey text;

  Dialogue() {
  }

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
