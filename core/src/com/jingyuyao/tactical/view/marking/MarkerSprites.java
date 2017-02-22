package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.AssetModule;
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
  MarkerSprites(AssetManager assetManager) {
    highlight = newSprite(assetManager, AssetModule.HIGHLIGHT);
    activated = newSprite(assetManager, AssetModule.ACTIVATED);
    move = newSprite(assetManager, AssetModule.MOVE);
    hit = newSprite(assetManager, AssetModule.HIT);
    targetSelect = newSprite(assetManager, AssetModule.TARGET_SELECT);
    attack = newSprite(assetManager, AssetModule.ATTACK);
  }

  private static Sprite newSprite(AssetManager assetManager, String name) {
    return new Sprite(assetManager.get(name, Texture.class));
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
