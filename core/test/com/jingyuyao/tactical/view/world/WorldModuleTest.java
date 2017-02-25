package com.jingyuyao.tactical.view.world;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldModuleTest {

  @Bind
  @Mock
  private AssetManager assetManager;
  @Bind
  @Mock
  private Batch batch;
  @Bind
  @Mock
  private ControllerFactory controllerFactory;
  @Bind
  @Mock
  private ActorFactory actorFactory;
  @Mock
  private GL20 gl20;
  @Mock
  private Texture texture;

  @Inject
  private World world;
  @Inject
  private WorldSubscriber worldSubscriber;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Before
  public void setUp() {
    Gdx.graphics = new MockGraphics();
    Gdx.gl = gl20;
    when(assetManager.get(anyString(), eq(Texture.class))).thenReturn(texture);
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new WorldModule()).injectMembers(this);
  }
}