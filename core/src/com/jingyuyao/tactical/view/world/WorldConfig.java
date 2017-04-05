package com.jingyuyao.tactical.view.world;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldConfig {

  @Inject
  WorldConfig() {

  }

  public int getTileSize() {
    return 32;
  }

  int getWorldViewportWidth() {
    return 16;
  }

  int getWorldViewportHeight() {
    return 9;
  }

  float getTileToWorldScale() {
    return 1f / getTileSize();
  }
}
