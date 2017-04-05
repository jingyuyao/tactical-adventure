package com.jingyuyao.tactical.view.resource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.view.world2.WorldConfig;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceModuleTest {

  @Bind
  @Mock
  private WorldConfig worldConfig;

  @Inject
  private Markers markers;
  @Inject
  private Animations animations;
  @Inject
  private AnimationTime animationTime;
  @Inject
  private Skin provideSkin;
  @Inject
  private TextureAtlas provideTextureAtlas;
  @Inject
  private TextureFactory textureFactory;

  @Before
  public void setUp() {
    when(worldConfig.getTileSize()).thenReturn(32);
  }

  @Test
  public void can_create_module() {
    Injector injector = Guice
        .createInjector(BoundFieldModule.of(this), new MockGameModule(), new ResourceModule());

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