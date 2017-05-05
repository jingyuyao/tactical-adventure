package com.jingyuyao.tactical.view.world.component;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;

public class SingleAnimation extends AbstractAnimation {

  private final Promise promise = new Promise();
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
      promise.complete();
    }
  }

  public boolean isDone() {
    return getAnimation().isAnimationFinished(stateTime);
  }

  public Promise getPromise() {
    return promise;
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.NORMAL;
  }
}
