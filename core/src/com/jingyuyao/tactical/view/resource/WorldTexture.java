package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.view.actor.ActorConfig;
import com.jingyuyao.tactical.view.world.WorldConfig;
import javax.inject.Inject;

public class WorldTexture {

  private final TextureRegion textureRegion;
  private final float worldSize;
  private final float worldOffset;

  @Inject
  WorldTexture(
      @Assisted TextureRegion textureRegion, WorldConfig worldConfig, ActorConfig actorConfig) {
    Preconditions.checkArgument(textureRegion.getRegionHeight() == textureRegion.getRegionWidth());
    this.textureRegion = textureRegion;
    int scale = textureRegion.getRegionHeight() / worldConfig.getTileSize();
    this.worldSize = actorConfig.getActorWorldSize() * scale;
    this.worldOffset = (actorConfig.getActorWorldSize() / 2f) * (scale - 1);
  }

  public void draw(Batch batch, float x, float y) {
    batch.draw(textureRegion, x - worldOffset, y - worldOffset, worldSize, worldSize);
  }
}
