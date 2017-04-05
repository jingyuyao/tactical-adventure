package com.jingyuyao.tactical.view.world2;

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

  public float getWorldUnit() {
    return 1f;
  }

  int getWorldViewportWidth() {
    return 16;
  }

  int getWorldViewportHeight() {
    return 9;
  }

  float getTileToWorldScale() {
    return getWorldUnit() / getTileSize();
  }
}
