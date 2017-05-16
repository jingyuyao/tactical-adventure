package com.jingyuyao.tactical.view.world.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
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

  private static final String SHIP_ASSET_PREFIX = "ship/";
  private static final String SHIP_NAME = "me";
  private static final String SHIP_ASSET = SHIP_ASSET_PREFIX + SHIP_NAME;
  private static final String WEAPON_ASSET_PREFIX = "weapon/";
  private static final String WEAPON_NAME = "sword";
  private static final String WEAPON_ASSET = WEAPON_ASSET_PREFIX + WEAPON_NAME;
  private static final int SHIP_IDLE_FPS = 5;
  private static final int WEAPON_FPS = 10;

  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private TextureFactory textureFactory;
  @Mock
  private Map<String, LoopAnimation> loopAnimationCache;
  @Mock
  private Map<String, WorldTexture[]> worldTextureCache;
  @Mock
  private LoopAnimation mockLoopAnimation;
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
            resourceConfig, textureAtlas, textureFactory, loopAnimationCache, worldTextureCache);
  }

  @Test
  public void get_ship_empty_no_regions() {
    when(resourceConfig.getShipAssetPrefix()).thenReturn(SHIP_ASSET_PREFIX);
    when(resourceConfig.getShipIdleFPS()).thenReturn(SHIP_IDLE_FPS);
    when(loopAnimationCache.containsKey(SHIP_ASSET)).thenReturn(false);
    when(worldTextureCache.containsKey(SHIP_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(SHIP_ASSET)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);

    LoopAnimation animation = animations.getShip(SHIP_NAME);

    verify(loopAnimationCache).put(eq(SHIP_ASSET), animationCaptor.capture());
    verify(worldTextureCache).put(eq(SHIP_ASSET), worldTexturesCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(animation);
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
  }

  @Test
  public void get_ship_empty_has_regions() {
    when(resourceConfig.getShipAssetPrefix()).thenReturn(SHIP_ASSET_PREFIX);
    when(resourceConfig.getShipIdleFPS()).thenReturn(SHIP_IDLE_FPS);
    when(loopAnimationCache.containsKey(SHIP_ASSET)).thenReturn(false);
    when(worldTextureCache.containsKey(SHIP_ASSET)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(SHIP_ASSET)).thenReturn(cached);

    LoopAnimation animation = animations.getShip(SHIP_NAME);

    verify(loopAnimationCache).put(eq(SHIP_ASSET), animationCaptor.capture());
    verifyZeroInteractions(textureAtlas);
    assertThat(animationCaptor.getValue()).isSameAs(animation);
  }

  @Test
  public void get_ship_not_empty() {
    when(resourceConfig.getShipAssetPrefix()).thenReturn(SHIP_ASSET_PREFIX);
    when(loopAnimationCache.containsKey(SHIP_ASSET)).thenReturn(true);
    when(loopAnimationCache.get(SHIP_ASSET)).thenReturn(mockLoopAnimation);

    assertThat(animations.getShip(SHIP_NAME)).isSameAs(mockLoopAnimation);
    verify(loopAnimationCache).containsKey(SHIP_ASSET);
    verify(loopAnimationCache).get(SHIP_ASSET);
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

    SingleAnimation animation = animations.getWeapon(WEAPON_NAME);

    verify(worldTextureCache).put(eq(WEAPON_ASSET), worldTexturesCaptor.capture());
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
    assertThat(animation.getKeyFrame()).isSameAs(worldTexture);
  }

  @Test
  public void get_weapon_has_region() {
    when(resourceConfig.getWeaponAssetPrefix()).thenReturn(WEAPON_ASSET_PREFIX);
    when(resourceConfig.getWeaponFPS()).thenReturn(WEAPON_FPS);
    when(worldTextureCache.containsKey(WEAPON_ASSET)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(WEAPON_ASSET)).thenReturn(cached);

    SingleAnimation animation = animations.getWeapon(WEAPON_NAME);

    assertThat(animation.getKeyFrame()).isSameAs(worldTexture);
    verifyZeroInteractions(textureAtlas);
  }
}