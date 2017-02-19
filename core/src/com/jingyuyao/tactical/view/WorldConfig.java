package com.jingyuyao.tactical.view;

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

  public float getActorSize() {
    return 1f;
  }

  public int getTileSize() {
    return 32;
  }

  public float getTileToWorldScale() {
    return 1f / getTileSize();
  }

  public int getUIScale() {
    return 50;
  }
}
