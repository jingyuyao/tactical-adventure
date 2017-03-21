package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoopAnimationTest {

  private static final int FPS = 5;

  @Mock
  private AnimationTime animationTime;
  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;

  private LoopAnimation loopAnimation;

  @Before
  public void setUp() {
    loopAnimation = new LoopAnimation(FPS, new WorldTexture[]{texture1, texture2}, animationTime);
  }

  @Test
  public void get_current_frame_1() {
    when(animationTime.getStateTime()).thenReturn(0.1f);

    assertThat(loopAnimation.getCurrentFrame()).isSameAs(texture1);
  }

  @Test
  public void get_current_frame_2() {
    when(animationTime.getStateTime()).thenReturn(0.3f);

    assertThat(loopAnimation.getCurrentFrame()).isSameAs(texture2);
  }

  @Test
  public void get_current_frame_loopback() {
    when(animationTime.getStateTime()).thenReturn(0.5f);

    assertThat(loopAnimation.getCurrentFrame()).isSameAs(texture1);
  }
}
