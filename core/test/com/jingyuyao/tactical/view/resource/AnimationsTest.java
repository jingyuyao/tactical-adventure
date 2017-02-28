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
  private static final String WEAPON_ASSET_PREFIX = "weapon/";
  private static final String WEAPON_NAME = "sword";
  private static final String WEAPON_ASSET = WEAPON_ASSET_PREFIX + WEAPON_NAME;
  private static final int CHARACTER_IDLE_FPS = 5;
  private static final int WEAPON_FPS = 10;

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
  private LoopAnimation mockLoopAnimation;
  @Mock
  private SingleAnimation mockSingleAnimation;
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
  public void get_character_empty_no_regions() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(atlasRegionsCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(CHARACTER_ASSET)).thenReturn(textureRegions);
    when(animationFactory.createLoop(CHARACTER_IDLE_FPS, textureRegions)).thenReturn(
        mockLoopAnimation);

    LoopAnimation animation = animations.getCharacter(CHARACTER_NAME);

    verify(loopAnimationCache).put(eq(CHARACTER_ASSET), animationCaptor.capture());
    verify(atlasRegionsCache).put(eq(CHARACTER_ASSET), atlasRegionsCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(mockLoopAnimation);
    assertThat(atlasRegionsCaptor.getValue()).isSameAs(textureRegions);
    assertThat(animation).isSameAs(mockLoopAnimation);
  }

  @Test
  public void get_character_empty_has_regions() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(atlasRegionsCache.containsKey(CHARACTER_ASSET)).thenReturn(true);
    when(atlasRegionsCache.get(CHARACTER_ASSET)).thenReturn(textureRegions);
    when(animationFactory.createLoop(CHARACTER_IDLE_FPS, textureRegions)).thenReturn(
        mockLoopAnimation);

    LoopAnimation animation = animations.getCharacter(CHARACTER_NAME);

    verify(loopAnimationCache).put(eq(CHARACTER_ASSET), animationCaptor.capture());
    verifyZeroInteractions(textureAtlas);
    assertThat(animationCaptor.getValue()).isSameAs(mockLoopAnimation);
    assertThat(animation).isSameAs(mockLoopAnimation);
  }

  @Test
  public void get_character_not_empty() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(true);
    when(loopAnimationCache.get(CHARACTER_ASSET)).thenReturn(mockLoopAnimation);

    assertThat(animations.getCharacter(CHARACTER_NAME)).isSameAs(mockLoopAnimation);
    verify(loopAnimationCache).containsKey(CHARACTER_ASSET);
    verify(loopAnimationCache).get(CHARACTER_ASSET);
    verifyNoMoreInteractions(loopAnimationCache);
    verifyZeroInteractions(textureAtlas);
  }

  @Test
  public void get_weapon_no_regions() {
    when(resourceConfig.getWeaponAssetPrefix()).thenReturn(WEAPON_ASSET_PREFIX);
    when(resourceConfig.getWeaponFPS()).thenReturn(WEAPON_FPS);
    when(atlasRegionsCache.containsKey(WEAPON_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(WEAPON_ASSET)).thenReturn(textureRegions);
    when(animationFactory.createSingle(WEAPON_FPS, textureRegions)).thenReturn(mockSingleAnimation);

    SingleAnimation animation = animations.getWeapon(WEAPON_NAME);

    verify(atlasRegionsCache).put(eq(WEAPON_ASSET), atlasRegionsCaptor.capture());
    assertThat(atlasRegionsCaptor.getValue()).isSameAs(textureRegions);
    assertThat(animation).isSameAs(mockSingleAnimation);
  }

  @Test
  public void get_weapon_has_region() {
    when(resourceConfig.getWeaponAssetPrefix()).thenReturn(WEAPON_ASSET_PREFIX);
    when(resourceConfig.getWeaponFPS()).thenReturn(WEAPON_FPS);
    when(atlasRegionsCache.containsKey(WEAPON_ASSET)).thenReturn(true);
    when(atlasRegionsCache.get(WEAPON_ASSET)).thenReturn(textureRegions);

    when(animationFactory.createSingle(WEAPON_FPS, textureRegions)).thenReturn(mockSingleAnimation);

    SingleAnimation animation = animations.getWeapon(WEAPON_NAME);

    verifyZeroInteractions(textureAtlas);
    assertThat(animation).isSameAs(mockSingleAnimation);
  }
}