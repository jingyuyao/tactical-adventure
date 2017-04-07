package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.screen.play.PlayMenu;
import com.jingyuyao.tactical.view.WorldScreen;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
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
    Guice.createInjector(new GameModule(this)).injectMembers(this);

    modelEventBus.register(gameState);
    worldScreen.register(modelEventBus);
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
