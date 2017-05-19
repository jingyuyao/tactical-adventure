package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.ShowDialogues;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Dialogue;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class BattleSequence {

  private final ModelBus modelBus;
  private final WorldState worldState;
  private final LevelComplete levelComplete;

  @Inject
  BattleSequence(ModelBus modelBus, WorldState worldState, LevelComplete levelComplete) {
    this.modelBus = modelBus;
    this.worldState = worldState;
    this.levelComplete = levelComplete;
  }

  void start(final Battle battle, final Runnable done) {
    modelBus.post(new StartBattle(battle, new Promise(new Runnable() {
      @Override
      public void run() {
        processDeaths(battle.getDeaths(), 0, done);
      }
    })));
  }

  private void processDeaths(final List<Person> death, final int index, final Runnable done) {
    if (index < death.size()) {
      ResourceKey name = death.get(index).getName();
      List<Dialogue> dialogues = worldState.getScript().getDeathDialogues().get(name);
      if (dialogues.isEmpty()) {
        processDeaths(death, index + 1, done);
      } else {
        modelBus.post(new ShowDialogues(dialogues, new Promise(new Runnable() {
          @Override
          public void run() {
            processDeaths(death, index + 1, done);
          }
        })));
      }
    } else {
      levelComplete.check(done);
    }
  }
}
