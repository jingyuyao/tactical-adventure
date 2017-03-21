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
  private TextureFactory textureFactory;
  @Mock
  private Map<String, LoopAnimation> loopAnimationCache;
  @Mock
  private Map<String, WorldTexture[]> worldTextureCache;
  @Mock
  private LoopAnimation mockLoopAnimation;
  @Mock
  private SingleAnimation mockSingleAnimation;
  @Mock
  private AtlasRegion atlasRegion;
  @Mock
  private WorldTexture worldTexture;
  @Captor
  private ArgumentCaptor<LoopAnimation> animationCaptor;
  @Captor
  private ArgumentCaptor<WorldTexture[]> worldTexturesCaptor;

  private Array<AtlasRegion> atlasRegions;
  private Animations animations;

  @Before
  public void setUp() {
    atlasRegions = new Array<>();
    atlasRegions.add(atlasRegion);
    animations =
        new Animations(
            resourceConfig, textureAtlas, animationFactory, textureFactory, loopAnimationCache,
            worldTextureCache);
  }

  @Test
  public void get_character_empty_no_regions() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(worldTextureCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(CHARACTER_ASSET)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);
    when(animationFactory.createLoop(eq(CHARACTER_IDLE_FPS), worldTexturesCaptor.capture()))
        .thenReturn(mockLoopAnimation);

    LoopAnimation animation = animations.getCharacter(CHARACTER_NAME);

    verify(loopAnimationCache).put(eq(CHARACTER_ASSET), animationCaptor.capture());
    verify(worldTextureCache).put(CHARACTER_ASSET, worldTexturesCaptor.getValue());
    assertThat(animationCaptor.getValue()).isSameAs(mockLoopAnimation);
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
    assertThat(animation).isSameAs(mockLoopAnimation);
  }

  @Test
  public void get_character_empty_has_regions() {
    when(resourceConfig.getCharacterAssetPrefix()).thenReturn(CHARACTER_ASSET_PREFIX);
    when(resourceConfig.getCharacterIdleFPS()).thenReturn(CHARACTER_IDLE_FPS);
    when(loopAnimationCache.containsKey(CHARACTER_ASSET)).thenReturn(false);
    when(worldTextureCache.containsKey(CHARACTER_ASSET)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(CHARACTER_ASSET)).thenReturn(cached);
    when(animationFactory.createLoop(CHARACTER_IDLE_FPS, cached))
        .thenReturn(mockLoopAnimation);

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
    when(worldTextureCache.containsKey(WEAPON_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(WEAPON_ASSET)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);
    when(animationFactory.createSingle(eq(WEAPON_FPS), worldTexturesCaptor.capture()))
        .thenReturn(mockSingleAnimation);

    SingleAnimation animation = animations.getWeapon(WEAPON_NAME);

    verify(worldTextureCache).put(WEAPON_ASSET, worldTexturesCaptor.getValue());
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
    assertThat(animation).isSameAs(mockSingleAnimation);
  }

  @Test
  public void get_weapon_has_region() {
    when(resourceConfig.getWeaponAssetPrefix()).thenReturn(WEAPON_ASSET_PREFIX);
    when(resourceConfig.getWeaponFPS()).thenReturn(WEAPON_FPS);
    when(worldTextureCache.containsKey(WEAPON_ASSET)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(WEAPON_ASSET)).thenReturn(cached);
    when(animationFactory.createSingle(WEAPON_FPS, cached)).thenReturn(mockSingleAnimation);

    SingleAnimation animation = animations.getWeapon(WEAPON_NAME);

    verifyZeroInteractions(textureAtlas);
    assertThat(animation).isSameAs(mockSingleAnimation);
  }
}