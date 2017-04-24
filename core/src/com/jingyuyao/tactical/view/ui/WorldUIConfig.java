package com.jingyuyao.tactical.view.ui;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class WorldUIConfig {

  @Inject
  WorldUIConfig() {
  }

  int getUIWidth() {
    return 700;
  }

  int getUIHeight() {
    return 450;
  }
}
