package com.jingyuyao.tactical;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.inject.AbstractModule;

/**
 * Provides mocks for {@link Gdx} and {@link com.kotcrab.vis.ui.VisUI} related classes.
 */
public class MockGameModule extends AbstractModule {

  @Override
  protected void configure() {
    HeadlessNativesLoader.load();

    Gdx.app = mock(Application.class);
    Gdx.files = new HeadlessFiles();
    Gdx.graphics = new MockGraphics();
    Gdx.input = mock(Input.class);
    Gdx.gl = mock(GL20.class);

    // We need to populate Gdx.files before we can load this
    VisLoader.load();

    bind(Application.class).toInstance(Gdx.app);
    bind(Files.class).toInstance(Gdx.files);
    bind(Graphics.class).toInstance(Gdx.graphics);
    bind(Input.class).toInstance(Gdx.input);
    bind(GL20.class).toInstance(Gdx.gl);
    bind(AssetManager.class).toInstance(mock(AssetManager.class));
    bind(TextureAtlas.class).toInstance(mock(TextureAtlas.class));
    bind(Batch.class).toInstance(mock(Batch.class));
  }
}
