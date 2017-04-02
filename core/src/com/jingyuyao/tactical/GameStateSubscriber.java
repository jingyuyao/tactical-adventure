package com.jingyuyao.tactical;

import com.badlogic.gdx.Application;
import com.google.common.eventbus.DeadEvent;
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
  private final Application application;

  @Inject
  GameStateSubscriber(
      GameState gameState,
      WorldScreenSubscriber worldScreenSubscriber,
      Application application) {
    this.gameState = gameState;
    this.worldScreenSubscriber = worldScreenSubscriber;
    this.application = application;
  }

  void register(EventBus eventBus) {
    eventBus.register(this);
    worldScreenSubscriber.register(eventBus);
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    gameState.advanceLevel();
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    gameState.replayLevel();
  }

  @Subscribe
  void deadEvent(DeadEvent deadEvent) {
    application.log("GameStateSubscriber", deadEvent.getEvent().toString());
  }
}
