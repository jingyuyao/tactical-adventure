package com.jingyuyao.tactical.view;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.inject.util.Modules;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.resource.ResourceModule;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ViewModuleTest {

  @Bind
  @Mock
  private AssetManager assetManager;
  @Bind
  @Mock
  private WorldController worldController;
  @Bind
  @Mock
  private ControllerFactory controllerFactory;
  @Bind
  @Mock
  private Batch batch;
  @Mock
  private GL20 gl20;
  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private Skin skin;

  @Inject
  private WorldScreen worldScreen;
  @Inject
  private WorldScreenSubscribers worldScreenSubscribers;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Before
  public void setUp() {
    Gdx.graphics = new MockGraphics();
    Gdx.files = new HeadlessFiles();
    Gdx.gl = gl20;
    when(assetManager.get(ResourceModule.TEXTURE_ATLAS, TextureAtlas.class))
        .thenReturn(textureAtlas);
    when(assetManager.get(ResourceModule.SKIN, Skin.class)).thenReturn(skin);
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), Modules.override(new ViewModule()).with(
        new AbstractModule() {
          @Override
          protected void configure() {
            // The real sprite batch cannot be easily created in a headless environment so
            // we will just mock it instead.
            bind(Batch.class).toInstance(batch);
          }
        })).injectMembers(this);
  }
}