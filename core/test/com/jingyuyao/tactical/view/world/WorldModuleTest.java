package com.jingyuyao.tactical.view.world;

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
import com.jingyuyao.tactical.view.resource.Animations;
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
  @Bind
  @Mock
  private Animations animations;
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
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new WorldModule()).injectMembers(this);
  }
}