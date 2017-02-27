package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MarkerSprites {

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

  Sprite getHighlight() {
    return highlight;
  }

  Sprite getActivated() {
    return activated;
  }

  Sprite getMove() {
    return move;
  }

  Sprite getHit() {
    return hit;
  }

  Sprite getTargetSelect() {
    return targetSelect;
  }

  Sprite getAttack() {
    return attack;
  }
}
