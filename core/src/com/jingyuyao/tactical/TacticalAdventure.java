package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.jingyuyao.tactical.screen.StartScreen;
import com.jingyuyao.tactical.screen.WorldScreen;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  private GameState gameState;

  @Inject
  private WorldScreen worldScreen;
  @Inject
  private StartScreen startScreen;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    VisUI.load(SkinScale.X2);
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
    worldScreen.dispose();
    startScreen.dispose();
    assetManager.dispose();
    VisUI.dispose();
  }

  void goToPlayMenu() {
    setScreen(startScreen);
  }

  void goToWorldScreen() {
    setScreen(worldScreen);
  }

  boolean isAtWorldScreen() {
    return getScreen().equals(worldScreen);
  }
}
