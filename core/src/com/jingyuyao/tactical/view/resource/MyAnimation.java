package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.google.inject.assistedinject.Assisted;
import javax.inject.Inject;

public class MyAnimation extends AbstractAnimation {

  private final AnimationTime animationTime;

  @Inject
  MyAnimation(
      @Assisted int fps,
      @Assisted Array<? extends TextureRegion> keyFrames,
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
