package com.jingyuyao.tactical.view.resource;

import javax.inject.Singleton;

@Singleton
public class AnimationTime {

  private float stateTime = 0f;

  public void advanceStateTime(float delta) {
    stateTime += delta;
  }

  float getStateTime() {
    return stateTime;
  }
}
