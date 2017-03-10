package com.jingyuyao.tactical;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.LevelComplete;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameSubscriber {

  private final TacticalAdventure game;

  @Inject
  GameSubscriber(TacticalAdventure game) {
    this.game = game;
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    Gdx.app.log("TA", "level complete!");
  }

  @Subscribe
  void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
  }
}
