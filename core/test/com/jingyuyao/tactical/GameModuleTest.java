package com.jingyuyao.tactical;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.util.Modules;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameModuleTest {

  @Mock
  private TacticalAdventure game;
  @Mock
  private Application application;
  @Mock
  private Input input;
  @Mock
  private GL20 gl;
  @Mock
  private Batch batch;

  @Inject
  private GameState gameState;

  @BeforeClass
  public static void setUpClass() {
    HeadlessNativesLoader.load();
  }

  @Before
  public void setUp() {
    Gdx.app = application;
    Gdx.files = new HeadlessFiles();
    Gdx.graphics = new MockGraphics();
    Gdx.input = input;
    Gdx.gl = gl;
  }

  @Test
  public void can_create_module() {
    // NOTE: This test will only pass if the working direction is set to android/assets/
    Guice.createInjector(Modules.override(new GameModule(game)).with(new AbstractModule() {
      @Override
      protected void configure() {
        // We can't instantiate SpriteBatch in a GUI-less environment
        bind(Batch.class).toInstance(batch);
      }
    })).injectMembers(this);
  }
}