package com.jingyuyao.tactical.view.resource;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.view.world2.WorldConfig;
import javax.inject.Inject;

public class WorldTexture implements Component {

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

  public void draw(Batch batch, Actor actor) {
    if (actor != null) {
      draw(batch, actor.getX(), actor.getY());
    }
  }
}
