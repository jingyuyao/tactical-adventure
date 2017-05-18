package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Dialogue {

  private final ResourceKey name;
  private final ResourceKey text;

  public Dialogue(ResourceKey name, ResourceKey text) {
    this.name = name;
    this.text = text;
  }

  public ResourceKey getName() {
    return name;
  }

  public ResourceKey getText() {
    return text;
  }
}
