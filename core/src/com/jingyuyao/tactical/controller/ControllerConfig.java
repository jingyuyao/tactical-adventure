package com.jingyuyao.tactical.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ControllerConfig {

  @Inject
  ControllerConfig() {

  }

  float getDragScreenCutoff() {
    return 10f;
  }
}
