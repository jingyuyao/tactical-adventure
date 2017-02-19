package com.jingyuyao.tactical.view.actor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ActorConfig {

  @Inject
  ActorConfig() {

  }

  float getActorSize() {
    return 1f;
  }
}
