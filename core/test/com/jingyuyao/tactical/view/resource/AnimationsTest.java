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
  private static final String CHARACTER_NAME = "me";
  private static final String CHARACTER_ASSET = CHARACTER_ASSET_PREFIX + CHARACTER_NAME;
  private static final int CHARACTER_IDLE_FPS = 5;

  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private AnimationFactory animationFactory;
  @Mock
  private Map<String, LoopAnimation> loopAnimationCache;
  @Mock
  private Map<String, Array<AtlasRegion>> atlasRegionsCache;
  @Mock
  private LoopAnimation mockAnimation;
  @Mock
  private Array<AtlasRegion> textureRegions;
  @Captor
  private ArgumentCaptor<LoopAnimation> animationCaptor;
  @Captor
  private ArgumentCaptor<Array<AtlasRegion>> atlasRegionsCaptor;

  private Animations animations;

  @Before
  public void setUp() {
    animations =
        new Animations(
            resourceConfig, textureAtlas, animationFactory, loopAnimationCache, atlasRegionsCache);
  }

  @Test
  public void get_loop_empty_no_regions() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(atlasRegionsCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(CHARACTER_ASSET)).thenReturn(textureRegions);
    when(animationFactory.createLoop(CHARACTER_IDLE_FPS, textureRegions)).thenReturn(mockAnimation);

    LoopAnimation animation = animations.getCharacter(CHARACTER_NAME);

    verify(loopAnimationCache).put(eq(CHARACTER_ASSET), animationCaptor.capture());
    verify(atlasRegionsCache).put(eq(CHARACTER_ASSET), atlasRegionsCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(mockAnimation);
    assertThat(atlasRegionsCaptor.getValue()).isSameAs(textureRegions);
    assertThat(animation).isSameAs(mockAnimation);
  }

  @Test
  public void get_loop_empty_has_regions() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(atlasRegionsCache.containsKey(CHARACTER_ASSET)).thenReturn(true);
    when(atlasRegionsCache.get(CHARACTER_ASSET)).thenReturn(textureRegions);
    when(animationFactory.createLoop(CHARACTER_IDLE_FPS, textureRegions)).thenReturn(mockAnimation);

    LoopAnimation animation = animations.getCharacter(CHARACTER_NAME);

    verify(loopAnimationCache).put(eq(CHARACTER_ASSET), animationCaptor.capture());
    verifyZeroInteractions(textureAtlas);
    assertThat(animationCaptor.getValue()).isSameAs(mockAnimation);
    assertThat(animation).isSameAs(mockAnimation);
  }

  @Test
  public void get_not_empty() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET_PREFIX + CHARACTER_NAME)).thenReturn(true);
    when(loopAnimationCache.get(CHARACTER_ASSET_PREFIX + CHARACTER_NAME)).thenReturn(mockAnimation);

    assertThat(animations.getCharacter(CHARACTER_NAME)).isSameAs(mockAnimation);
    verify(loopAnimationCache).containsKey(CHARACTER_ASSET_PREFIX + CHARACTER_NAME);
    verify(loopAnimationCache).get(CHARACTER_ASSET_PREFIX + CHARACTER_NAME);
    verifyNoMoreInteractions(loopAnimationCache);
    verifyZeroInteractions(textureAtlas);
  }
}