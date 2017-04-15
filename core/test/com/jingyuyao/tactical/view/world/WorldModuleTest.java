package com.jingyuyao.tactical.view.world;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.controller.WorldCamera;
import com.jingyuyao.tactical.controller.WorldController;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldModuleTest {

  @Bind
  @Mock
  private WorldCamera worldCamera;
  @Bind
  @Mock
  private WorldController worldController;

  @Inject
  private WorldView worldView;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Test
  public void can_create_module() {
    Injector injector = Guice.createInjector(
        BoundFieldModule.of(this),
        new MockGameModule(),
        new WorldModule());

    AssetManager assetManager = injector.getInstance(AssetManager.class);
    AtlasRegion atlasRegion = mock(AtlasRegion.class);
    when(atlasRegion.getRegionHeight()).thenReturn(10);
    when(atlasRegion.getRegionWidth()).thenReturn(10);

    TextureAtlas textureAtlas = mock(TextureAtlas.class);
    when(textureAtlas.findRegion(anyString())).thenReturn(atlasRegion);
    when(assetManager.get(anyString(), eq(TextureAtlas.class))).thenReturn(textureAtlas);

    injector.injectMembers(this);
  }
}