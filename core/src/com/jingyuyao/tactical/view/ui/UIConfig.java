package com.jingyuyao.tactical.view.ui;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class UIConfig {

  @Inject
  UIConfig() {
  }

  int getUIViewportWidth() {
    return 700;
  }

  int getUIViewportHeight() {
    return 450;
  }
}
