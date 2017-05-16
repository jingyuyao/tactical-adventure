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
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.ship.Ship;
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

  private static final String SHIP_ASSET = "ship";
  private static final String ITEM_ASSET = "sword";
  private static final int SHIP_IDLE_FPS = 5;
  private static final int ITEM_FPS = 10;

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
  @Mock
  private Ship ship;
  @Mock
  private Item item;
  @Mock
  private ResourceKey resourceKey;
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
    when(resourceConfig.getShipIdleFPS()).thenReturn(SHIP_IDLE_FPS);
    when(loopAnimationCache.containsKey(SHIP_ASSET)).thenReturn(false);
    when(worldTextureCache.containsKey(SHIP_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(SHIP_ASSET)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);
    when(ship.getAnimation()).thenReturn(resourceKey);
    when(resourceKey.getRaw()).thenReturn(SHIP_ASSET);

    LoopAnimation animation = animations.get(ship);

    verify(loopAnimationCache).put(eq(SHIP_ASSET), animationCaptor.capture());
    verify(worldTextureCache).put(eq(SHIP_ASSET), worldTexturesCaptor.capture());
    assertThat(animationCaptor.getValue()).isSameAs(animation);
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
  }

  @Test
  public void get_ship_empty_has_regions() {
    when(resourceConfig.getShipIdleFPS()).thenReturn(SHIP_IDLE_FPS);
    when(loopAnimationCache.containsKey(SHIP_ASSET)).thenReturn(false);
    when(worldTextureCache.containsKey(SHIP_ASSET)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(SHIP_ASSET)).thenReturn(cached);
    when(ship.getAnimation()).thenReturn(resourceKey);
    when(resourceKey.getRaw()).thenReturn(SHIP_ASSET);

    LoopAnimation animation = animations.get(ship);

    verify(loopAnimationCache).put(eq(SHIP_ASSET), animationCaptor.capture());
    verifyZeroInteractions(textureAtlas);
    assertThat(animationCaptor.getValue()).isSameAs(animation);
  }

  @Test
  public void get_ship_not_empty() {
    when(loopAnimationCache.containsKey(SHIP_ASSET)).thenReturn(true);
    when(loopAnimationCache.get(SHIP_ASSET)).thenReturn(mockLoopAnimation);
    when(ship.getAnimation()).thenReturn(resourceKey);
    when(resourceKey.getRaw()).thenReturn(SHIP_ASSET);

    assertThat(animations.get(ship)).isSameAs(mockLoopAnimation);
    verify(loopAnimationCache).containsKey(SHIP_ASSET);
    verify(loopAnimationCache).get(SHIP_ASSET);
    verifyNoMoreInteractions(loopAnimationCache);
    verifyZeroInteractions(textureAtlas);
  }

  @Test
  public void get_item_no_regions() {
    when(resourceConfig.getItemFPS()).thenReturn(ITEM_FPS);
    when(worldTextureCache.containsKey(ITEM_ASSET)).thenReturn(false);
    when(textureAtlas.findRegions(ITEM_ASSET)).thenReturn(atlasRegions);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);
    when(item.getAnimation()).thenReturn(resourceKey);
    when(resourceKey.getRaw()).thenReturn(ITEM_ASSET);

    SingleAnimation animation = animations.get(item);

    verify(worldTextureCache).put(eq(ITEM_ASSET), worldTexturesCaptor.capture());
    assertThat(worldTexturesCaptor.getValue()).asList().containsExactly(worldTexture);
    assertThat(animation.getKeyFrame()).isSameAs(worldTexture);
  }

  @Test
  public void get_item_has_region() {
    when(resourceConfig.getItemFPS()).thenReturn(ITEM_FPS);
    when(worldTextureCache.containsKey(ITEM_ASSET)).thenReturn(true);
    WorldTexture[] cached = new WorldTexture[]{worldTexture};
    when(worldTextureCache.get(ITEM_ASSET)).thenReturn(cached);
    when(item.getAnimation()).thenReturn(resourceKey);
    when(resourceKey.getRaw()).thenReturn(ITEM_ASSET);

    SingleAnimation animation = animations.get(item);

    assertThat(animation.getKeyFrame()).isSameAs(worldTexture);
    verifyZeroInteractions(textureAtlas);
  }
}