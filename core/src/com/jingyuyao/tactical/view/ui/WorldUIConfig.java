package com.jingyuyao.tactical.view.ui;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class WorldUIConfig {

  @Inject
  WorldUIConfig() {
  }

  int getUIWidth() {
    return 1920;
  }

  int getUIHeight() {
    return 1080;
  }
}
