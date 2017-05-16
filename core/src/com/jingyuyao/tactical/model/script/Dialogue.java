package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.resource.ResourceKey;

public class Dialogue {

  private final ResourceKey name;
  private final ResourceKey resourceKey;

  public Dialogue(ResourceKey name, ResourceKey resourceKey) {
    this.name = name;
    this.resourceKey = resourceKey;
  }

  public ResourceKey getName() {
    return name;
  }

  public ResourceKey getResourceKey() {
    return resourceKey;
  }
}
