package com.jingyuyao.tactical.view.world.component;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;

public class SingleAnimation extends AbstractAnimation {

  private final MyFuture future = new MyFuture();
  private float stateTime = 0f;

  public SingleAnimation(int fps, WorldTexture[] keyFrames) {
    super(fps, keyFrames);
  }

  public WorldTexture getKeyFrame() {
    return getAnimation().getKeyFrame(stateTime);
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
