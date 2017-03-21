package com.jingyuyao.tactical.view.actor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActorConfig {

  @Inject
  ActorConfig() {

  }

  public float getActorWorldSize() {
    return 1f;
  }

  float getMoveTimePerUnit() {
    return 0.06f;
  }
}
