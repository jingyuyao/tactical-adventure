package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.google.inject.Stage;
import com.jingyuyao.tactical.menu.LevelResultMenu;
import com.jingyuyao.tactical.menu.LevelResultMenu.LevelResult;
import com.jingyuyao.tactical.menu.StartMenu;
import com.jingyuyao.tactical.view.GameScreen;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  private GameState gameState;

  @Inject
  private GameScreen gameScreen;
  @Inject
  private StartMenu startMenu;
  @Inject
  private LevelResultMenu levelResultMenu;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    Guice.createInjector(Stage.PRODUCTION, new GameModule(this)).injectMembers(this);

    gameState.goToStartMenu();
  }

  @Override
  public void dispose() {
    super.dispose();
    gameScreen.dispose();
    startMenu.dispose();
    assetManager.dispose();
  }

  void goToStartMenu() {
    setScreen(startMenu);
  }

  void goToLevelResultMenu(LevelResult levelResult) {
    levelResultMenu.setLevelResult(levelResult);
    setScreen(levelResultMenu);
  }

  void goToWorldScreen() {
    setScreen(gameScreen);
  }
}
