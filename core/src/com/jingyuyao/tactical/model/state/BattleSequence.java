package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Dialogue;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class BattleSequence {

  private final ModelBus modelBus;
  private final WorldState worldState;
  private final ScriptRunner scriptRunner;

  @Inject
  BattleSequence(ModelBus modelBus, WorldState worldState, ScriptRunner scriptRunner) {
    this.modelBus = modelBus;
    this.worldState = worldState;
    this.scriptRunner = scriptRunner;
  }

  void start(final Battle battle, final Runnable done) {
    modelBus.post(new StartBattle(battle, new Promise(new Runnable() {
      @Override
      public void run() {
        processDeaths(battle.getDeaths().iterator(), done);
      }
    })));
  }

  private void processDeaths(final Iterator<Person> deathIterator, final Runnable done) {
    if (deathIterator.hasNext()) {
      ResourceKey name = deathIterator.next().getName();
      List<Dialogue> dialogues = worldState.getScript().getDeathDialogues().get(name);
      if (dialogues.isEmpty()) {
        processDeaths(deathIterator, done);
      } else {
        modelBus.post(new ShowDialogues(dialogues, new Promise(new Runnable() {
          @Override
          public void run() {
            processDeaths(deathIterator, done);
          }
        })));
      }
    } else {
      scriptRunner.check(done);
    }
  }
}
