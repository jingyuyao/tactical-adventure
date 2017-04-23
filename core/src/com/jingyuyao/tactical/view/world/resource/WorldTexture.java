package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.WorldConfig;
import javax.inject.Inject;

public class WorldTexture {

  private final TextureRegion textureRegion;
  private final float worldWidth;
  private final float worldHeight;
  private final float worldXOffset;
  private final float worldYOffset;
  private final float originY;

  @Inject
  WorldTexture(@Assisted TextureRegion textureRegion, WorldConfig worldConfig) {
    this.textureRegion = textureRegion;
    this.worldWidth = textureRegion.getRegionWidth() / worldConfig.getTileSize();
    this.worldHeight = textureRegion.getRegionHeight() / worldConfig.getTileSize();
    this.worldXOffset = 0.5f * (worldWidth - 1);
    this.worldYOffset = 0.5f * (worldHeight - 1);
    this.originY = worldHeight / 2;
  }

  /**
   * Draw this texture centered at world coordinate (x,y).
   */
  public void draw(Batch batch, float x, float y) {
    batch.draw(textureRegion, x - worldXOffset, y - worldYOffset, worldWidth, worldHeight);
  }

  /**
   * Draw this texture at world coordinate (x, y) facing a specific direction.
   *
   * Assumes the texture is drawn to accommodate rotation with an origin at (0.5, height/2)
   * in world coordinates.
   */
  public void draw(Batch batch, float x, float y, Direction direction) {
    switch (direction) {
      case UP:
        draw(batch, x, y, 90f);
        break;
      case DOWN:
        draw(batch, x, y, -90f);
        break;
      case LEFT:
        draw(batch, x, y, true);
        break;
      case RIGHT:
        draw(batch, x, y, false);
        break;
    }
  }

  private void draw(Batch batch, float x, float y, float rotation) {
    batch.draw(
        textureRegion,
        x, y - worldYOffset,
        0.5f, originY,
        worldWidth, worldHeight,
        1f, 1f,
        rotation);
  }

  private void draw(Batch batch, float x, float y, boolean flip) {
    batch.draw(
        textureRegion,
        x, y - worldYOffset,
        0.5f, originY,
        worldWidth, worldHeight,
        flip ? -1f : 1f, 1f,
        0f);
  }
}
