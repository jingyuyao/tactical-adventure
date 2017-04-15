package com.jingyuyao.tactical.view.world.component;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;

public class LoopAnimation extends AbstractAnimation {

  public LoopAnimation(int fps, WorldTexture[] keyFrames) {
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
