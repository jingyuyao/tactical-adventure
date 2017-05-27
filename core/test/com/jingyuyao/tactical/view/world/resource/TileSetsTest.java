package com.jingyuyao.tactical.view.world.resource;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TileSetsTest {

  private static final String ASSET_PATH = "some/path/to/tileset";
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
    when(assetManager.isLoaded(BUNDLE.getPath())).thenReturn(false);
    when(assetManager.get(BUNDLE.getPath())).thenReturn(tileSet);
    when(tileSet.getWidth()).thenReturn(20);
    when(tileSet.getHeight()).thenReturn(10);
    when(worldConfig.getTileSize()).thenReturn(10);
    when(textureFactory.create(any(TextureRegion.class))).thenReturn(worldTexture1, worldTexture2);

    assertThat(tileSets.get(BUNDLE.get(1))).isSameAs(worldTexture1);
    assertThat(tileSets.get(BUNDLE.get(2))).isSameAs(worldTexture2);
    assertThat(tileTextureCache).doesNotContainKey(BUNDLE.get(0));
    assertThat(tileTextureCache).doesNotContainKey(BUNDLE.get(3));
  }
}