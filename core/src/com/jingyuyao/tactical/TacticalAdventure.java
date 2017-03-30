package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.view.ViewModule;
import com.jingyuyao.tactical.view.WorldScreen;
import com.jingyuyao.tactical.view.WorldScreenSubscribers;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
  @Inject
  private WorldScreen worldScreen;
  @Inject
  private WorldScreenSubscribers worldScreenSubscribers;
  @Inject
  private GameState gameState;
  @Inject
  private AssetManager assetManager;

  @Override
  public void create() {
    Guice
        .createInjector(
            new GameModule(this),
            new ModelModule(),
            new DataModule(),
            new ViewModule(),
            new ControllerModule())
        .injectMembers(this);

    worldScreenSubscribers.register(modelEventBus);
    modelEventBus.register(gameState);
    gameState.continueLevel();
  }

  @Override
  public void pause() {
    super.pause();
    gameState.saveProgress();
  }

  @Override
  public void dispose() {
    super.dispose();
    worldScreen.dispose();
    assetManager.dispose();
  }

  void goToWorldScreen() {
    setScreen(worldScreen);
  }
}
