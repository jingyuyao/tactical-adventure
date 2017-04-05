package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.jingyuyao.tactical.model.event.MyFuture;

public class SingleAnimation extends AbstractAnimation {

  private final MyFuture future = new MyFuture();
  private float stateTime = 0f;

  SingleAnimation(int fps, WorldTexture[] keyFrames) {
    super(fps, keyFrames);
  }

  public WorldTexture getKeyFrame() {
    return getKeyFrame(stateTime);
  }

  public void advanceTime(float delta) {
    stateTime += delta;
    if (isDone()) {
      future.done();
    }
  }

  public boolean isDone() {
    return getAnimation().isAnimationFinished(stateTime);
  }

  public MyFuture getFuture() {
    return future;
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.NORMAL;
  }
}
