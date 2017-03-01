package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkerSprites {

  private final WorldTexture highlight;
  private final WorldTexture activated;
  private final Sprite move;
  private final Sprite hit;
  private final Sprite targetSelect;
  private final Sprite attack;

  @Inject
  MarkerSprites(TextureAtlas textureAtlas, TextureFactory textureFactory) {
    highlight = textureFactory.create(textureAtlas.findRegion("marking/highlight"));
    activated = textureFactory.create(textureAtlas.findRegion("marking/activated"));
    move = textureAtlas.createSprite("marking/move");
    hit = textureAtlas.createSprite("marking/hit");
    targetSelect = textureAtlas.createSprite("marking/target_select");
    attack = textureAtlas.createSprite("marking/attack");
  }

  public WorldTexture getHighlight() {
    return highlight;
  }

  public WorldTexture getActivated() {
    return activated;
  }

  public Sprite getMove() {
    return move;
  }

  public Sprite getHit() {
    return hit;
  }

  public Sprite getTargetSelect() {
    return targetSelect;
  }

  public Sprite getAttack() {
    return attack;
  }
}
