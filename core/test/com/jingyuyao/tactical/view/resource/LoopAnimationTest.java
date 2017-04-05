package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoopAnimationTest {

  private static final int FPS = 5;

  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;

  private LoopAnimation loopAnimation;

  @Before
  public void setUp() {
    loopAnimation = new LoopAnimation(FPS, new WorldTexture[]{texture1, texture2});
  }

  @Test
  public void get_current_frame_1() {
    assertThat(loopAnimation.getKeyFrame(0.1f)).isSameAs(texture1);
  }

  @Test
  public void get_current_frame_2() {
    assertThat(loopAnimation.getKeyFrame(0.3f)).isSameAs(texture2);
  }

  @Test
  public void get_current_frame_loopback() {
    assertThat(loopAnimation.getKeyFrame(0.5f)).isSameAs(texture1);
  }
}
