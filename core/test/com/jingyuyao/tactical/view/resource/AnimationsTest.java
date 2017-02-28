package com.jingyuyao.tactical.view.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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

  private static final String CHARACTER_ASSET_PREFIX = "character/";
  private static final String KEY = "character/me";
  private static final int CHARACTER_IDLE_FPS = 5;

  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private Map<String, MyAnimation> animationMap;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private MyAnimationFactory myAnimationFactory;
  @Mock
  private MyAnimation mockAnimation;
  @Mock
  private Array<AtlasRegion> textureRegions;
  @Captor
  private ArgumentCaptor<MyAnimation> animationCaptor;

  private Animations animations;

  @Before
  public void setUp() {
    animations = new Animations(resourceConfig, animationMap, textureAtlas, myAnimationFactory);
  }

  @Test
  public void get_empty() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(animationMap.containsKey(CHARACTER_ASSET_PREFIX + KEY)).thenReturn(false);
    when(textureAtlas.findRegions(CHARACTER_ASSET_PREFIX + KEY)).thenReturn(textureRegions);
    when(myAnimationFactory.create(CHARACTER_IDLE_FPS, textureRegions)).thenReturn(mockAnimation);

    MyAnimation animation = animations.getCharacter(KEY);

    verify(animationMap).put(eq(CHARACTER_ASSET_PREFIX + KEY), animationCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(mockAnimation);
    assertThat(animation).isSameAs(mockAnimation);
  }

  @Test
  public void get_not_empty() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(animationMap.containsKey(CHARACTER_ASSET_PREFIX + KEY)).thenReturn(true);
    when(animationMap.get(CHARACTER_ASSET_PREFIX + KEY)).thenReturn(mockAnimation);

    assertThat(animations.getCharacter(KEY)).isSameAs(mockAnimation);
    verify(animationMap).containsKey(CHARACTER_ASSET_PREFIX + KEY);
    verify(animationMap).get(CHARACTER_ASSET_PREFIX + KEY);
    verifyNoMoreInteractions(animationMap);
    verifyZeroInteractions(textureAtlas);
  }
}