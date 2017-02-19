package com.jingyuyao.tactical.view.actor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ActorConfig {

  @Inject
  ActorConfig() {

  }

  float getActorWorldSize() {
    return 1f;
  }

  float getMoveTimePerUnit() {
    return 0.06f;
  }
}
