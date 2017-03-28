package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

class GameModule extends AbstractModule {

  private final TacticalAdventure game;

  GameModule(TacticalAdventure game) {
    this.game = game;
  }

  @Override
  protected void configure() {
    bind(TacticalAdventure.class).toInstance(game);
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    return new AssetManager();
  }
}
