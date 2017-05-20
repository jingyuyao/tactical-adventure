package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.world.World;
import java.util.Iterator;
import java.util.List;
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
   * Trigger scripts given the current {@link Turn} and {@link World}.
   */
  void triggerScripts(final Runnable keepGoing) {
    triggerDialogues(new Runnable() {
      @Override
      public void run() {
        triggerWinLose(keepGoing);
      }
    });
  }

  private void triggerDialogues(Runnable done) {
    triggerDialogues(worldState.getScript().getDialogues().keySet().iterator(), done);
  }

  private void triggerDialogues(final Iterator<Condition> conditions, final Runnable done) {
    if (conditions.hasNext()) {
      Turn turn = worldState.getTurn();
      final Condition condition = conditions.next();
      if (!condition.isTriggered() && condition.isMet(turn, world)) {
        List<Dialogue> dialogues = worldState.getScript().getDialogues().get(condition);
        modelBus.post(new ShowDialogues(dialogues, new Promise(new Runnable() {
          @Override
          public void run() {
            // can't consider a dialogue trigger until it finishes showing
            condition.triggered();
            triggerDialogues(conditions, done);
          }
        })));
      } else {
        triggerDialogues(conditions, done);
      }
    } else {
      done.run();
    }
  }

  private void triggerWinLose(Runnable keepGoing) {
    Script script = worldState.getScript();
    Turn turn = worldState.getTurn();
    // lose condition goes first so YOLOs are not encouraged
    for (Condition condition : script.getLoseConditions()) {
      if (condition.isMet(turn, world)) {
        if (condition.isTriggered()) {
          throw new RuntimeException("Lose condition should not trigger twice!");
        }
        condition.triggered();
        modelBus.post(new LevelLost());
        return;
      }
    }
    for (Condition condition : script.getWinConditions()) {
      if (condition.isMet(turn, world)) {
        if (condition.isTriggered()) {
          throw new RuntimeException("Win condition should not trigger twice!");
        }
        condition.triggered();
        modelBus.post(new LevelWon());
        return;
      }
    }
    keepGoing.run();
  }
}
