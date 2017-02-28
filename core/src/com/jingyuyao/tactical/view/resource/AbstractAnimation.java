package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.google.common.base.Preconditions;

abstract class AbstractAnimation {

  private final Animation<TextureRegion> animation;

  AbstractAnimation(int fps, Array<? extends TextureRegion> keyFrames) {
    Preconditions.checkNotNull(keyFrames);
    Preconditions.checkArgument(keyFrames.size > 0, "did you forget to pack textures?");
    this.animation = new Animation<>(1f / fps, keyFrames, getPlayMode());
  }

  public TextureRegion getCurrentFrame() {
    return animation.getKeyFrame(getStateTime());
  }

  abstract PlayMode getPlayMode();

  abstract float getStateTime();
}
