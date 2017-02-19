package com.jingyuyao.tactical.view.world;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldConfig {

  @Inject
  WorldConfig() {

  }

  public int getWorldWidth() {
    return 16;
  }

  public int getWorldHeight() {
    return 9;
  }

  float getTileToWorldScale() {
    return 1f / getTileSize();
  }

  private int getTileSize() {
    return 32;
  }
}
