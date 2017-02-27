package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AnimationTimeTest {

  private AnimationTime animationTime;

  @Before
  public void setUp() {
    animationTime = new AnimationTime();

    assertThat(animationTime.getStateTime()).isEqualTo(0f);
  }

  @Test
  public void advance_state_time() {
    animationTime.advanceStateTime(2f);

    assertThat(animationTime.getStateTime()).isEqualTo(2f);
  }
}