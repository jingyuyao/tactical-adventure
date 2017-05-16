package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.script.ScriptActions;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class BattleSequence {

  private final ModelBus modelBus;
  private final WorldState worldState;

  @Inject
  BattleSequence(ModelBus modelBus, WorldState worldState) {
    this.modelBus = modelBus;
    this.worldState = worldState;
  }

  void start(final Battle battle, final Runnable done) {
    modelBus.post(new StartBattle(battle, new Promise(new Runnable() {
      @Override
      public void run() {
        executeActionsAsync(battle.getDeath(), 0, done);
      }
    })));
  }

  private void executeActionsAsync(final List<Person> death, final int index, final Runnable done) {
    if (index < death.size()) {
      Message name = death.get(index).getName();
      Optional<ScriptActions> actionsOpt = worldState.getScript().deathScript(name);
      if (actionsOpt.isPresent()) {
        actionsOpt.get().execute(modelBus, new Runnable() {
          @Override
          public void run() {
            executeActionsAsync(death, index + 1, done);
          }
        });
      } else {
        executeActionsAsync(death, index + 1, done);
      }
    } else {
      done.run();
    }
  }
}
