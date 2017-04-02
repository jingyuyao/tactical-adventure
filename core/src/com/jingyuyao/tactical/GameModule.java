package com.jingyuyao.tactical;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.menu.MenuModule;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.view.ViewModule;
import javax.inject.Singleton;

class GameModule extends AbstractModule {

  private static final String SKIN = "ui/uiskin.json";

  private final TacticalAdventure game;

  GameModule(TacticalAdventure game) {
    this.game = game;
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
    install(new ViewModule());
    install(new ControllerModule());
    install(new DataModule());
    install(new MenuModule());
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    AssetManager assetManager = new AssetManager();
    assetManager.setLoader(TiledMap.class, new TmxMapLoader());
    return assetManager;
  }

  @Provides
  @Singleton
  Skin provideSkin(AssetManager assetManager) {
    assetManager.load(SKIN, Skin.class);
    assetManager.finishLoadingAsset(SKIN);
    return assetManager.get(SKIN, Skin.class);
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return new SpriteBatch();
  }
}
