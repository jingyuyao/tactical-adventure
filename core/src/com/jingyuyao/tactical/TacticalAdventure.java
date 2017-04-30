package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.jingyuyao.tactical.menu.StartScreen;
import com.jingyuyao.tactical.view.GameScreen;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  private GameState gameState;

  @Inject
  private GameScreen gameScreen;
  @Inject
  private StartScreen startScreen;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    Guice.createInjector(new GameModule(this)).injectMembers(this);

    gameState.start();
  }

  @Override
  public void pause() {
    super.pause();
    gameState.pause();
  }

  @Override
  public void dispose() {
    super.dispose();
    gameScreen.dispose();
    startScreen.dispose();
    assetManager.dispose();
  }

  void goToPlayMenu() {
    setScreen(startScreen);
  }

  void goToWorldScreen() {
    setScreen(gameScreen);
  }

  boolean isAtWorldScreen() {
    return getScreen().equals(gameScreen);
  }
}
