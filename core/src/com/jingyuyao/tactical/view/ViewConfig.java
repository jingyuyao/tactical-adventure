package com.jingyuyao.tactical.view;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewConfig {

  private static final int TILE_SIZE = 32; // pixels
  private static final float TILE_TO_WORLD_SCALE = 1f / TILE_SIZE;
  private static final int VIEWPORT_WORLD_WIDTH = 15; // # tiles
  private static final int VIEWPORT_WORLD_HEIGHT = 10; // # tiles
  private static final float ACTOR_WORLD_SIZE = 1f; // world units

  @Inject
  ViewConfig() {

  }

  public float getActorWorldSize() {
    return ACTOR_WORLD_SIZE;
  }

  public float getTileToWorldScale() {
    return TILE_TO_WORLD_SCALE;
  }

  public int getViewportWorldWidth() {
    return VIEWPORT_WORLD_WIDTH;
  }

  public int getViewportWorldHeight() {
    return VIEWPORT_WORLD_HEIGHT;
  }
}
