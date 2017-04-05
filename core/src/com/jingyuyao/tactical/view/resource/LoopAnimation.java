package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class LoopAnimation extends AbstractAnimation {

  LoopAnimation(int fps, WorldTexture[] keyFrames) {
    super(fps, keyFrames);
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.LOOP;
  }
}
