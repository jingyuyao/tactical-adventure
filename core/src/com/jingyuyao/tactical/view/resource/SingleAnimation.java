package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.view.resource.ResourceModule.AnimationBus;
import javax.inject.Inject;

public class SingleAnimation extends AbstractAnimation {

  private final EventBus animationBus;
  private final SettableFuture<Void> future;
  private float stateTime = 0f;

  @Inject
  SingleAnimation(
      @Assisted int fps,
      @Assisted Array<? extends TextureRegion> keyFrames,
      @AnimationBus EventBus animationBus) {
    super(fps, keyFrames);
    this.animationBus = animationBus;
    future = SettableFuture.create();
    animationBus.register(this);
  }

  @Override
  PlayMode getPlayMode() {
    return PlayMode.NORMAL;
  }

  @Override
  float getStateTime() {
    return stateTime;
  }

  @Subscribe
  void advanceTime(AdvanceTime advanceTime) {
    stateTime += advanceTime.getDelta();
    if (getAnimation().isAnimationFinished(stateTime)) {
      animationBus.unregister(this);
      future.set(null);
    }
  }

  public ListenableFuture<Void> getFuture() {
    return future;
  }
}