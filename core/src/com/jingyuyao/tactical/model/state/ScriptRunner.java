package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelLost;
import com.jingyuyao.tactical.model.event.LevelWon;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
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
   * Trigger any script action at the current turn. {@code keepGoing} is called if the level is not
   * terminated at this turn.
   */
  void triggerTurn(final Runnable keepGoing) {
    Script script = worldState.getScript();
    Turn turn = worldState.getTurn();
    List<Dialogue> dialogues = script.getTurnDialogues().get(turn);
    if (dialogues.isEmpty()) {
      triggerWinLose(keepGoing);
    } else {
      modelBus.post(new ShowDialogues(dialogues, new Promise(new Runnable() {
        @Override
        public void run() {
          triggerWinLose(keepGoing);
        }
      })));
    }
  }

  /**
   * Trigger any script action for the persons just died. {@code keepGoing} is called if the level
   * is not terminated by the deaths.
   */
  void triggerDeaths(List<Person> deaths, Runnable keepGoing) {
    triggerDeaths(deaths.iterator(), keepGoing);
  }

  private void triggerDeaths(final Iterator<Person> deathIterator, final Runnable keepGoing) {
    if (deathIterator.hasNext()) {
      ResourceKey name = deathIterator.next().getName();
      List<Dialogue> dialogues = worldState.getScript().getDeathDialogues().get(name);
      if (dialogues.isEmpty()) {
        triggerDeaths(deathIterator, keepGoing);
      } else {
        modelBus.post(new ShowDialogues(dialogues, new Promise(new Runnable() {
          @Override
          public void run() {
            triggerDeaths(deathIterator, keepGoing);
          }
        })));
      }
    } else {
      triggerWinLose(keepGoing);
    }
  }

  private void triggerWinLose(Runnable keepGoing) {
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
    keepGoing.run();
  }
}
