package com.jingyuyao.tactical.view;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewConfig {

  private static final int TILE_SIZE = 32; // pixels
  private static final float TILE_TO_WORLD_SCALE = 1f / TILE_SIZE;

  @Inject
  ViewConfig() {

  }

  float getTileToWorldScale() {
    return TILE_TO_WORLD_SCALE;
  }

  int getViewportWorldWidth() {
    return 15;
  }

  int getViewportWorldHeight() {
    return 10;
  }
}
