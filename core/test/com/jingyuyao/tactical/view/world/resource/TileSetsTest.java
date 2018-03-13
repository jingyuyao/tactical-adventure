package com.jingyuyao.tactical.view.world.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.view.world.WorldConfig;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TileSetsTest {

  private static final String ASSET_PATH = "some/path";
  private static final KeyBundle BUNDLE = KeyBundle.texture(ASSET_PATH);

  @Mock
  private AssetManager assetManager;
  @Mock
  private WorldConfig worldConfig;
  @Mock
  private TextureFactory textureFactory;
  @Mock
  private Texture tileSet;
  @Mock
  private WorldTexture worldTexture1;
  @Mock
  private WorldTexture worldTexture2;
  @Mock
  private WorldTexture worldTexture3;
  @Mock
  private WorldTexture worldTexture4;
  @Captor
  private ArgumentCaptor<TextureRegion> regionCaptor;

  private Map<IntKey, WorldTexture> tileTextureCache;
  private TileSets tileSets;

  @Before
  public void setUp() {
    tileTextureCache = new HashMap<>();
    tileSets = new TileSets(assetManager, worldConfig, textureFactory, tileTextureCache);
  }

  @Test
  public void get_key_cached() {
    tileTextureCache.put(BUNDLE.get(1), worldTexture1);

    assertThat(tileSets.get(BUNDLE.get(1))).isSameAs(worldTexture1);
  }

  @Test
  public void get_key_not_cached() {
    when(assetManager.isLoaded(BUNDLE.getPathWithExtension())).thenReturn(false);
    when(assetManager.get(BUNDLE.getPathWithExtension())).thenReturn(tileSet);
    when(tileSet.getWidth()).thenReturn(20);
    when(tileSet.getHeight()).thenReturn(20);
    when(worldConfig.getTileSize()).thenReturn(10);
    when(textureFactory.create(any(TextureRegion.class)))
        .thenReturn(worldTexture1, worldTexture2, worldTexture3, worldTexture4);

    assertThat(tileSets.get(BUNDLE.get(0))).isSameAs(worldTexture1);
    assertThat(tileSets.get(BUNDLE.get(1))).isSameAs(worldTexture2);
    assertThat(tileSets.get(BUNDLE.get(2))).isSameAs(worldTexture3);
    assertThat(tileSets.get(BUNDLE.get(3))).isSameAs(worldTexture4);
    assertThat(tileTextureCache).doesNotContainKey(BUNDLE.get(4));
    verify(textureFactory, times(4)).create(regionCaptor.capture());
    assertThat(regionCaptor.getAllValues()).hasSize(4);
    TextureRegion region1 = regionCaptor.getAllValues().get(0);
    assertThat(region1.getTexture()).isSameAs(tileSet);
    assertThat(region1.getRegionX()).isEqualTo(0);
    assertThat(region1.getRegionY()).isEqualTo(0);
    assertThat(region1.getRegionHeight()).isEqualTo(10);
    assertThat(region1.getRegionWidth()).isEqualTo(10);
    TextureRegion region2 = regionCaptor.getAllValues().get(1);
    assertThat(region2.getTexture()).isSameAs(tileSet);
    assertThat(region2.getRegionX()).isEqualTo(10);
    assertThat(region2.getRegionY()).isEqualTo(0);
    assertThat(region2.getRegionHeight()).isEqualTo(10);
    assertThat(region2.getRegionWidth()).isEqualTo(10);
    TextureRegion region3 = regionCaptor.getAllValues().get(2);
    assertThat(region3.getTexture()).isSameAs(tileSet);
    assertThat(region3.getRegionX()).isEqualTo(0);
    assertThat(region3.getRegionY()).isEqualTo(10);
    assertThat(region3.getRegionHeight()).isEqualTo(10);
    assertThat(region3.getRegionWidth()).isEqualTo(10);
    TextureRegion region4 = regionCaptor.getAllValues().get(3);
    assertThat(region4.getTexture()).isSameAs(tileSet);
    assertThat(region4.getRegionX()).isEqualTo(10);
    assertThat(region4.getRegionY()).isEqualTo(10);
    assertThat(region4.getRegionHeight()).isEqualTo(10);
    assertThat(region4.getRegionWidth()).isEqualTo(10);
  }
}