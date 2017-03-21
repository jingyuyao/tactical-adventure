package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UIModuleTest {

  @Bind
  @Mock
  private Skin skin;
  @Bind
  @Mock
  private Batch batch;
  @Mock
  private GL20 gl20;

  @Inject
  private UI ui;
  @Inject
  private UISubscriber uiSubscriber;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Before
  public void setUp() {
    Gdx.graphics = new MockGraphics();
    Gdx.files = new HeadlessFiles();
    Gdx.gl = gl20;
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new UIModule()).injectMembers(this);
  }
}