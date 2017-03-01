package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.google.inject.assistedinject.Assisted;
import javax.inject.Inject;

public class LoopAnimation extends AbstractAnimation {

  private final AnimationTime animationTime;

  @Inject
  LoopAnimation(
      @Assisted int fps,
      @Assisted WorldTexture[] keyFrames,
      AnimationTime animationTime) {
    super(fps, keyFrames);
    this.animationTime = animationTime;
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.LOOP;
  }

  @Override
  float getStateTime() {
    return animationTime.getStateTime();
  }
}
