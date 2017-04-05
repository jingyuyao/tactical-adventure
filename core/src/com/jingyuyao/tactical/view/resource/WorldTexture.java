package com.jingyuyao.tactical.view.resource;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.view.actor.ActorConfig;
import com.jingyuyao.tactical.view.world2.WorldConfig;
import javax.inject.Inject;

public class WorldTexture implements Component {

  private final TextureRegion textureRegion;
  private final float worldSize;
  private final float worldOffset;

  @Inject
  WorldTexture(
      @Assisted TextureRegion textureRegion, WorldConfig worldConfig, ActorConfig actorConfig) {
    Preconditions.checkArgument(textureRegion.getRegionHeight() == textureRegion.getRegionWidth());
    this.textureRegion = textureRegion;
    int scale = textureRegion.getRegionHeight() / worldConfig.getTileSize();
    // TODO: don't use actor size
    this.worldSize = actorConfig.getActorWorldSize() * scale;
    this.worldOffset = (actorConfig.getActorWorldSize() / 2f) * (scale - 1);
  }

  public void draw(Batch batch, float x, float y) {
    batch.draw(textureRegion, x - worldOffset, y - worldOffset, worldSize, worldSize);
  }

  public void draw(Batch batch, Actor actor) {
    if (actor != null) {
      draw(batch, actor.getX(), actor.getY());
    }
  }
}
