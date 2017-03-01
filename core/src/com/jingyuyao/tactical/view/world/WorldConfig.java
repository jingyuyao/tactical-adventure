package com.jingyuyao.tactical.view.world;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldConfig {

  @Inject
  WorldConfig() {

  }

  public int getWorldViewportWidth() {
    return 16;
  }

  public int getWorldViewportHeight() {
    return 9;
  }

  public int getTileSize() {
    return 32;
  }

  float getTileToWorldScale() {
    return 1f / getTileSize();
  }
}
