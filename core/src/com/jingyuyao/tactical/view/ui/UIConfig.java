package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.view.world.WorldConfig;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class UIConfig {

  private final WorldConfig worldConfig;

  @Inject
  UIConfig(WorldConfig worldConfig) {
    this.worldConfig = worldConfig;
  }

  int getUIViewportWidth() {
    return worldConfig.getWorldViewportWidth() * getUIScale();
  }

  int getUIViewportHeight() {
    return worldConfig.getWorldViewportHeight() * getUIScale();
  }

  private int getUIScale() {
    return 50;
  }
}
