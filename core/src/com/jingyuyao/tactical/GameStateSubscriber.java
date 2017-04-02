package com.jingyuyao.tactical;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.view.WorldScreenSubscriber;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameStateSubscriber {

  private final GameState gameState;
  private final WorldScreenSubscriber worldScreenSubscriber;

  @Inject
  GameStateSubscriber(
      GameState gameState,
      WorldScreenSubscriber worldScreenSubscriber) {
    this.gameState = gameState;
    this.worldScreenSubscriber = worldScreenSubscriber;
  }

  void register(EventBus eventBus) {
    eventBus.register(this);
    worldScreenSubscriber.register(eventBus);
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    gameState.finishLevel();
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    gameState.finishLevel();
  }
}
