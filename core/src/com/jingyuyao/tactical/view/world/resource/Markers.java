package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Markers {

  private final WorldTexture highlight;
  private final WorldTexture activated;
  private final WorldTexture move;
  private final WorldTexture targetSelect;
  private final WorldTexture attack;

  @Inject
  Markers(TextureAtlas textureAtlas, TextureFactory textureFactory) {
    highlight = textureFactory.create(textureAtlas.findRegion("marking/highlight"));
    activated = textureFactory.create(textureAtlas.findRegion("marking/activated"));
    move = textureFactory.create(textureAtlas.findRegion("marking/move"));
    targetSelect = textureFactory.create(textureAtlas.findRegion("marking/target_select"));
    attack = textureFactory.create(textureAtlas.findRegion("marking/attack"));
  }

  public WorldTexture getHighlight() {
    return highlight;
  }

  public WorldTexture getActivated() {
    return activated;
  }

  public WorldTexture getMove() {
    return move;
  }

  public WorldTexture getTargetSelect() {
    return targetSelect;
  }

  public WorldTexture getAttack() {
    return attack;
  }
}
