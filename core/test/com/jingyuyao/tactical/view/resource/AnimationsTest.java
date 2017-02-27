package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AnimationsTest {

  private static final String KEY = "character/me";
  private static final float DURATION = 0.2f;

  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private Map<String, Animation<TextureRegion>> animationMap;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private Animation<TextureRegion> mockAnimation;
  @Mock
  private Array<AtlasRegion> textureRegions;
  @Captor
  private ArgumentCaptor<Animation<TextureRegion>> animationCaptor;

  private Animations animations;

  @Before
  public void setUp() {
    animations = new Animations(resourceConfig, animationMap, textureAtlas);

    assertThat(animations.getStateTime()).isEqualTo(0f);
  }

  @Test
  public void advance_time() {
    animations.advanceStateTime(12f);

    assertThat(animations.getStateTime()).isEqualTo(12f);
  }

  @Test
  public void get_empty() {
    when(animationMap.containsKey(KEY)).thenReturn(false);
    when(textureAtlas.findRegions(KEY)).thenReturn(textureRegions);
    when(resourceConfig.getFrameDuration()).thenReturn(DURATION);

    Animation<TextureRegion> animation = animations.get(KEY);

    verify(animationMap).put(eq(KEY), animationCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(animation);
    assertThat(animation.getFrameDuration()).isEqualTo(DURATION);
    assertThat(animation.getPlayMode()).isEqualTo(PlayMode.LOOP);
  }

  @Test
  public void get_not_empty() {
    when(animationMap.containsKey(KEY)).thenReturn(true);
    when(animationMap.get(KEY)).thenReturn(mockAnimation);

    assertThat(animations.get(KEY)).isSameAs(mockAnimation);
    verify(animationMap).containsKey(KEY);
    verify(animationMap).get(KEY);
    verifyNoMoreInteractions(animationMap);
    verifyZeroInteractions(textureAtlas);
    verifyZeroInteractions(resourceConfig);
  }
}