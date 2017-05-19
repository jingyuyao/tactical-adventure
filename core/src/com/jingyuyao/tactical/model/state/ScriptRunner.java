package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptRunner {

  private final ModelBus modelBus;
  private final World world;
  private final WorldState worldState;

  @Inject
  ScriptRunner(ModelBus modelBus, World world, WorldState worldState) {
    this.modelBus = modelBus;
    this.world = world;
    this.worldState = worldState;
  }

  /**
   * Check whether this level has been completed or not. If its not yet completed, {@code
   * notComplete} is called.
   */
  void check(Runnable notComplete) {
    Script script = worldState.getScript();
    Turn turn = worldState.getTurn();
    // lose condition goes first so YOLOs are not encouraged
    for (Condition condition : script.getLoseConditions()) {
      if (condition.isMet(turn, world)) {
        modelBus.post(new LevelLost());
        return;
      }
    }
    for (Condition condition : script.getWinConditions()) {
      if (condition.isMet(turn, world)) {
        modelBus.post(new LevelWon());
        return;
      }
    }
    notComplete.run();
  }
}
