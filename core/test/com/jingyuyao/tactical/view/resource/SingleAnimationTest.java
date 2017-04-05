package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingleAnimationTest {

  private static final int FPS = 1;

  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;

  private SingleAnimation singleAnimation;

  @Before
  public void setUp() {
    singleAnimation = new SingleAnimation(FPS, new WorldTexture[]{texture1, texture2});

    assertThat(singleAnimation.getFuture().isDone()).isFalse();
  }

  @Test
  public void advance_time_not_finished_1() {
    singleAnimation.advanceTime(0.5f);

    assertThat(singleAnimation.getKeyFrame()).isSameAs(texture1);
    assertThat(singleAnimation.getFuture().isDone()).isFalse();
  }

  @Test
  public void advance_time_not_finished_2() {
    singleAnimation.advanceTime(1.5f);

    assertThat(singleAnimation.getKeyFrame()).isSameAs(texture2);
    assertThat(singleAnimation.getFuture().isDone()).isFalse();
  }

  @Test
  public void advance_time_finished() {
    singleAnimation.advanceTime(2.5f);

    assertThat(singleAnimation.getFuture().isDone()).isTrue();
  }
}