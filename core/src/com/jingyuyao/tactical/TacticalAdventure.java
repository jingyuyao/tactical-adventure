package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.screen.WorldScreen;
import com.jingyuyao.tactical.screen.play.PlayMenu;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  private ModelBus modelBus;
  @Inject
  private GameState gameState;

  @Inject
  private WorldScreen worldScreen;
  @Inject
  private PlayMenu playMenu;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    VisUI.load(SkinScale.X2);
    Guice.createInjector(new GameModule(this)).injectMembers(this);

    modelBus.register(gameState);
    worldScreen.register(modelBus);
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
    playMenu.dispose();
    assetManager.dispose();
    VisUI.dispose();
  }

  void goToPlayMenu() {
    setScreen(playMenu);
  }

  void goToWorldScreen() {
    setScreen(worldScreen);
  }

  boolean isAtWorldScreen() {
    return getScreen().equals(worldScreen);
  }
}
