package com.jingyuyao.tactical.view.resource;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.google.common.base.Preconditions;

abstract class AbstractAnimation implements Component {

  private final Animation<WorldTexture> animation;

  AbstractAnimation(int fps, WorldTexture[] keyFrames) {
    Preconditions.checkNotNull(keyFrames);
    Preconditions.checkArgument(keyFrames.length > 0, "did you forget to pack textures?");
    this.animation = new Animation<>(1f / fps, keyFrames);
    this.animation.setPlayMode(getPlayMode());
  }

  public WorldTexture getKeyFrame(float stateTime) {
    return animation.getKeyFrame(stateTime);
  }

  Animation<WorldTexture> getAnimation() {
    return animation;
  }

  abstract PlayMode getPlayMode();
}
