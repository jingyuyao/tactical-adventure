package com.jingyuyao.tactical.view.resource;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.view.resource.ResourceModule.AnimationBus;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnimationTime {

  private final EventBus animationBus;
  private float stateTime = 0f;

  @Inject
  AnimationTime(@AnimationBus EventBus animationBus) {
    this.animationBus = animationBus;
  }

  public void advanceStateTime(float delta) {
    stateTime += delta;
    animationBus.post(new AdvanceTime(delta));
  }

  float getStateTime() {
    return stateTime;
  }
}
