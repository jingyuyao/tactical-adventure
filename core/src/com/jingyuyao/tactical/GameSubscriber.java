package com.jingyuyao.tactical;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.event.LevelComplete;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameSubscriber {

  private final TacticalAdventure game;
  private final Model model;

  @Inject
  GameSubscriber(TacticalAdventure game, Model model) {
    this.game = game;
    this.model = model;
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    model.reset();
    game.setLevel(TacticalAdventure.TEST_MAP);
  }

  @Subscribe
  void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
  }
}
