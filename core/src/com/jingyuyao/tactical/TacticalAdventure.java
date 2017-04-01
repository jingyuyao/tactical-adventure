package com.jingyuyao.tactical;

import com.badlogic.gdx.Game;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.jingyuyao.tactical.controller.ControllerModule;
import com.jingyuyao.tactical.data.DataModule;
import com.jingyuyao.tactical.model.ModelModule;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.view.ViewModule;
import javax.inject.Inject;

public class TacticalAdventure extends Game {

  @Inject
  @ModelEventBus
  private EventBus modelEventBus;
  @Inject
  private GameState gameState;
  @Inject
  private GameStateSubscriber gameStateSubscriber;

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

    gameStateSubscriber.register(modelEventBus);
    gameState.continueLevel();
  }

  @Override
  public void pause() {
    super.pause();
    gameState.pause();
  }

  @Override
  public void dispose() {
    super.dispose();
    gameState.dispose();
  }
}
