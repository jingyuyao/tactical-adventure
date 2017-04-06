package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class LoopAnimation extends AbstractAnimation {

  LoopAnimation(int fps, WorldTexture[] keyFrames) {
    super(fps, keyFrames);
  }

  public WorldTexture getKeyFrame(float stateTime) {
    return getAnimation().getKeyFrame(stateTime);
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.LOOP;
  }
}
