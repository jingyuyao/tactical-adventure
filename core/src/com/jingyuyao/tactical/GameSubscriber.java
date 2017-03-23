package com.jingyuyao.tactical;

import com.badlogic.gdx.Gdx;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.WorldSaver;
import com.jingyuyao.tactical.model.World;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class GameSubscriber {

  private final TacticalAdventure game;
  private final World world;
  private final WorldSaver worldSaver;

  @Inject
  GameSubscriber(TacticalAdventure game, World world, WorldSaver worldSaver) {
    this.game = game;
    this.world = world;
    this.worldSaver = worldSaver;
  }

  @Subscribe
  void levelComplete(LevelComplete levelComplete) {
    worldSaver.removeSave(TacticalAdventure.TEST_MAP);
    world.reset();
    game.setLevel(TacticalAdventure.TEST_MAP);
  }

  @Subscribe
  void levelFailed(LevelFailed levelFailed) {
    worldSaver.removeSave(TacticalAdventure.TEST_MAP);
    world.reset();
    game.setLevel(TacticalAdventure.TEST_MAP);
  }

  @Subscribe
  void logDeadEvent(DeadEvent deadEvent) {
    Gdx.app.log("DeadEvent", deadEvent.getEvent().toString());
  }
}
