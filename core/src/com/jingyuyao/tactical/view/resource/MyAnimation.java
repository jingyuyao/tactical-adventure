package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import javax.inject.Inject;

public class MyAnimation {

  private final Animation<TextureRegion> animation;
  private final AnimationTime animationTime;

  @Inject
  MyAnimation(
      ResourceConfig resourceConfig,
      AnimationTime animationTime,
      @Assisted Array<? extends TextureRegion> keyFrames) {
    Preconditions.checkNotNull(keyFrames);
    Preconditions.checkArgument(keyFrames.size > 0, "did you forget to pack textures?");
    this.animation = new Animation<>(resourceConfig.getFrameDuration(), keyFrames, PlayMode.LOOP);
    this.animationTime = animationTime;
  }

  public TextureRegion getCurrentFrame() {
    return animation.getKeyFrame(animationTime.getStateTime());
  }
}
