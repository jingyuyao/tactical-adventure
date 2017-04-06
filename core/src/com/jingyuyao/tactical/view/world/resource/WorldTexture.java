package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.view.world.WorldConfig;
import javax.inject.Inject;

public class WorldTexture {

  private final TextureRegion textureRegion;
  private final float worldWidth;
  private final float worldHeight;
  private final float worldXOffset;
  private final float worldYOffset;

  @Inject
  WorldTexture(@Assisted TextureRegion textureRegion, WorldConfig worldConfig) {
    this.textureRegion = textureRegion;
    this.worldWidth = textureRegion.getRegionWidth() / worldConfig.getTileSize();
    this.worldHeight = textureRegion.getRegionHeight() / worldConfig.getTileSize();
    this.worldXOffset = 0.5f * (worldWidth - 1);
    this.worldYOffset = 0.5f * (worldHeight - 1);
  }

  public void draw(Batch batch, float x, float y) {
    batch.draw(textureRegion, x - worldXOffset, y - worldYOffset, worldWidth, worldHeight);
  }
}
