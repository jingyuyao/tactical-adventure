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
import com.jingyuyao.tactical.model.resource.StringKey;
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

  private static final String ASSET_PATH = "some/path/to/asset";
  private static final int LOOP_FPS = 5;
  private static final int SINGLE_FPS = 10;

  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private TextureFactory textureFactory;
  @Mock
  private Map<StringKey, LoopAnimation> loopAnimationCache;
  @Mock
  private Map<StringKey, WorldTexture[]> worldTextureCache;
  @Mock
  private LoopAnimation mockLoopAnimation;
  @Mock
  private AtlasRegion atlasRegion;
  @Mock
  private WorldTexture worldTexture;
  @Mock
  private StringKey stringKey;
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
    when(resourceConfig.getLoopFps()).thenReturn(LOOP_FPS);
    when(loopAnimationCache.containsKey(stringKey)).thenReturn(false);
    when(worldTextureCache.containsKey(stringKey)).thenReturn(false);
    when(textureAtlas.findRegions(ASSET_PATH)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);
    when(stringKey.getPath()).thenReturn(ASSET_PATH);

    LoopAnimation animation = animations.getLoop(stringKey);

    verify(loopAnimationCache).put(eq(stringKey), animationCaptor.capture());
    verify(worldTextureCache).put(eq(stringKey), worldTexturesCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(animation);
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
  }

  @Test
  public void get_ship_empty_has_regions() {
    when(resourceConfig.getLoopFps()).thenReturn(LOOP_FPS);
    when(loopAnimationCache.containsKey(stringKey)).thenReturn(false);
    when(worldTextureCache.containsKey(stringKey)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(stringKey)).thenReturn(cached);

    LoopAnimation animation = animations.getLoop(stringKey);

    verify(loopAnimationCache).put(eq(stringKey), animationCaptor.capture());
    verifyZeroInteractions(textureAtlas);
    assertThat(animationCaptor.getValue()).isSameAs(animation);
  }

  @Test
  public void get_ship_not_empty() {
    when(loopAnimationCache.containsKey(stringKey)).thenReturn(true);
    when(loopAnimationCache.get(stringKey)).thenReturn(mockLoopAnimation);

    assertThat(animations.getLoop(stringKey)).isSameAs(mockLoopAnimation);
    verify(loopAnimationCache).containsKey(stringKey);
    verify(loopAnimationCache).get(stringKey);
    verifyNoMoreInteractions(loopAnimationCache);
    verifyZeroInteractions(textureAtlas);
  }

  @Test
  public void get_item_no_regions() {
    when(resourceConfig.getSingleFps()).thenReturn(SINGLE_FPS);
    when(worldTextureCache.containsKey(stringKey)).thenReturn(false);
    when(textureAtlas.findRegions(ASSET_PATH)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);
    when(stringKey.getPath()).thenReturn(ASSET_PATH);

    SingleAnimation animation = animations.getSingle(stringKey);

    verify(worldTextureCache).put(eq(stringKey), worldTexturesCaptor.capture());
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
    assertThat(animation.getKeyFrame()).isSameAs(worldTexture);
  }

  @Test
  public void get_item_has_region() {
    when(resourceConfig.getSingleFps()).thenReturn(SINGLE_FPS);
    when(worldTextureCache.containsKey(stringKey)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(stringKey)).thenReturn(cached);

    SingleAnimation animation = animations.getSingle(stringKey);

    assertThat(animation.getKeyFrame()).isSameAs(worldTexture);
    verifyZeroInteractions(textureAtlas);
  }
}