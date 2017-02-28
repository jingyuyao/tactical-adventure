package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingleAnimationTest {

  private static final int FPS = 1;

  @Mock
  private EventBus animationBus;
  @Mock
  private AtlasRegion region1;
  @Mock
  private AtlasRegion region2;
  @Mock
  private AdvanceTime advanceTime;

  private SingleAnimation singleAnimation;

  @Before
  public void setUp() {
    Array<AtlasRegion> regions = new Array<>();
    regions.add(region1);
    regions.add(region2);

    singleAnimation = new SingleAnimation(FPS, regions, animationBus);

    verify(animationBus).register(singleAnimation);
    assertThat(singleAnimation.getFuture().isDone()).isFalse();
  }

  @Test
  public void advance_time_not_finished_1() {
    when(advanceTime.getDelta()).thenReturn(0.5f);

    singleAnimation.advanceTime(advanceTime);

    assertThat(singleAnimation.getStateTime()).isEqualTo(0.5f);
    assertThat(singleAnimation.getCurrentFrame()).isSameAs(region1);
    assertThat(singleAnimation.getFuture().isDone()).isFalse();
    verifyNoMoreInteractions(animationBus);
  }

  @Test
  public void advance_time_not_finished_2() {
    when(advanceTime.getDelta()).thenReturn(1.5f);

    singleAnimation.advanceTime(advanceTime);

    assertThat(singleAnimation.getStateTime()).isEqualTo(1.5f);
    assertThat(singleAnimation.getCurrentFrame()).isSameAs(region2);
    assertThat(singleAnimation.getFuture().isDone()).isFalse();
    verifyNoMoreInteractions(animationBus);
  }

  @Test
  public void advance_time_finished() {
    when(advanceTime.getDelta()).thenReturn(2.5f);

    singleAnimation.advanceTime(advanceTime);

    assertThat(singleAnimation.getStateTime()).isEqualTo(2.5f);
    assertThat(singleAnimation.getFuture().isDone()).isTrue();
    verify(animationBus).unregister(singleAnimation);
  }
}