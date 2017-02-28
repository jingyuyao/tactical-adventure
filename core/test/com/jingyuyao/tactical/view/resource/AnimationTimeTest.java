package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnimationTimeTest {

  @Mock
  private EventBus animationBus;
  @Captor
  private ArgumentCaptor<AdvanceTime> advanceTimeCaptor;

  private AnimationTime animationTime;

  @Before
  public void setUp() {
    animationTime = new AnimationTime(animationBus);

    assertThat(animationTime.getStateTime()).isEqualTo(0f);
  }

  @Test
  public void advance_state_time() {
    animationTime.advanceStateTime(2f);

    assertThat(animationTime.getStateTime()).isEqualTo(2f);
    verify(animationBus).post(advanceTimeCaptor.capture());
    assertThat(advanceTimeCaptor.getValue().getDelta()).isEqualTo(2f);
  }
}