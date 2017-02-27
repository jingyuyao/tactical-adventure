package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyAnimationTest {

  private static final float DURATION = 0.2f;

  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private AnimationTime animationTime;
  @Mock
  private AtlasRegion region1;
  @Mock
  private AtlasRegion region2;

  private Array<AtlasRegion> regions;
  private MyAnimation myAnimation;

  @Before
  public void setUp() {
    when(resourceConfig.getFrameDuration()).thenReturn(DURATION);
    regions = new Array<>();
    regions.add(region1);
    regions.add(region2);

    myAnimation = new MyAnimation(resourceConfig, animationTime, regions);
  }

  @Test
  public void get_current_frame_1() {
    when(animationTime.getStateTime()).thenReturn(0.1f);

    assertThat(myAnimation.getCurrentFrame()).isSameAs(region1);
  }

  @Test
  public void get_current_frame_2() {
    when(animationTime.getStateTime()).thenReturn(0.3f);

    assertThat(myAnimation.getCurrentFrame()).isSameAs(region2);
  }

  @Test
  public void get_current_frame_loopback() {
    when(animationTime.getStateTime()).thenReturn(0.5f);

    assertThat(myAnimation.getCurrentFrame()).isSameAs(region1);
  }
}
