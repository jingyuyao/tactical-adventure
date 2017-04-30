package com.jingyuyao.tactical.view.ui;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameUIConfig {

  @Inject
  GameUIConfig() {
  }

  int getUIWidth() {
    return 1920;
  }

  int getUIHeight() {
    return 1080;
  }
}
