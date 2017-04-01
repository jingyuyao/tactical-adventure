package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

class GameModule extends AbstractModule {

  private final Game game;

  GameModule(Game game) {
    this.game = game;
  }

  @Override
  protected void configure() {
    bind(Game.class).toInstance(game);
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    return new AssetManager();
  }
}
