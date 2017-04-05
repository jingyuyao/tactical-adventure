package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

public class SingleAnimation extends AbstractAnimation {

  private final SettableFuture<Void> future;
  private float stateTime = 0f;

  SingleAnimation(int fps, WorldTexture[] keyFrames) {
    super(fps, keyFrames);
    future = SettableFuture.create();
  }

  public WorldTexture getKeyFrame() {
    return getKeyFrame(stateTime);
  }

  public void advanceTime(float delta) {
    stateTime += delta;
    if (isDone()) {
      future.set(null);
    }
  }

  public boolean isDone() {
    return getAnimation().isAnimationFinished(stateTime);
  }

  public ListenableFuture<Void> getFuture() {
    return future;
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.NORMAL;
  }
}
