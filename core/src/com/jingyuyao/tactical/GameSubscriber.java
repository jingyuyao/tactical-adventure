package com.jingyuyao.tactical;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.ModelSaver;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.event.LevelComplete;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameSubscriber {

  private final TacticalAdventure game;
  private final Model model;
  private final ModelSaver modelSaver;

  @Inject
  GameSubscriber(TacticalAdventure game, Model model, ModelSaver modelSaver) {
    this.game = game;
    this.model = model;
    this.modelSaver = modelSaver;
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    modelSaver.removeSave(TacticalAdventure.TEST_MAP);
    model.reset();
    game.setLevel(TacticalAdventure.TEST_MAP);
  }

  @Subscribe
  void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
  }
}
