package com.jingyuyao.tactical;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.menu.MenuModule;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.view.ViewModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Qualifier;
import javax.inject.Singleton;

class GameModule extends AbstractModule {

  private static final String TEXTURE_ATLAS = "drawable/texture.atlas";

  private final TacticalAdventure game;

  /**
   * Requires {@link Gdx} to be initialized.
   *
   * Also calls {@link VisLoader#load()}.
   */
  GameModule(TacticalAdventure game) {
    Preconditions.checkNotNull(Gdx.app);
    Preconditions.checkNotNull(Gdx.files);
    Preconditions.checkNotNull(Gdx.graphics);
    Preconditions.checkNotNull(Gdx.input);
    Preconditions.checkNotNull(Gdx.gl);
    this.game = game;
    VisLoader.load();
  }

  @Override
  protected void configure() {
    bind(TacticalAdventure.class).toInstance(game);
    bind(Application.class).toInstance(Gdx.app);
    bind(Files.class).toInstance(Gdx.files);
    bind(Graphics.class).toInstance(Gdx.graphics);
    bind(Input.class).toInstance(Gdx.input);
    bind(GL20.class).toInstance(Gdx.gl);

    install(new ModelModule());
    install(new ControllerModule());
    install(new ViewModule());
    install(new DataModule());
    install(new MenuModule());
  }

  @Provides
  @Singleton
  @BackgroundExecutor
  ExecutorService provideBackgroundExecutor() {
    return Executors.newSingleThreadExecutor();
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    return new AssetManager();
  }

  @Provides
  @Singleton
  TextureAtlas provideTextureAtlas(AssetManager assetManager) {
    assetManager.load(TEXTURE_ATLAS, TextureAtlas.class);
    assetManager.finishLoadingAsset(TEXTURE_ATLAS);
    return assetManager.get(TEXTURE_ATLAS, TextureAtlas.class);
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return new SpriteBatch();
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface BackgroundExecutor {

  }
}
