package com.jingyuyao.tactical.view.world.resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ResourceConfig {

  @Inject
  ResourceConfig() {

  }

  int getLoopFps() {
    return 5;
  }

  int getSingleFps() {
    return 10;
  }
}
