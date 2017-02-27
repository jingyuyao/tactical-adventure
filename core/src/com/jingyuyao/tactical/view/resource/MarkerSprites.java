package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MarkerSprites {

  private final Sprite highlight;
  private final Sprite activated;
  private final Sprite move;
  private final Sprite hit;
  private final Sprite targetSelect;
  private final Sprite attack;

  @Inject
  MarkerSprites(TextureAtlas textureAtlas) {
    highlight = textureAtlas.createSprite("marking/highlight");
    activated = textureAtlas.createSprite("marking/activated");
    move = textureAtlas.createSprite("marking/move");
    hit = textureAtlas.createSprite("marking/hit");
    targetSelect = textureAtlas.createSprite("marking/target_select");
    attack = textureAtlas.createSprite("marking/attack");
  }

  public Sprite getHighlight() {
    return highlight;
  }

  public Sprite getActivated() {
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
