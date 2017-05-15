package com.jingyuyao.tactical.model.battle;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.WorldState;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BattleSequence {

  private final ModelBus modelBus;
  private final WorldState worldState;

  @Inject
  BattleSequence(ModelBus modelBus, WorldState worldState) {
    this.modelBus = modelBus;
    this.worldState = worldState;
  }

  public void start(final Battle battle, final Runnable done) {
    modelBus.post(new StartBattle(battle, new Promise(new Runnable() {
      @Override
      public void run() {
        executeActionsAsync(battle.getDeath(), 0, done);
      }
    })));
  }

  private void executeActionsAsync(
      final List<Character> death, final int index, final Runnable done) {
    if (index < death.size()) {
      String nameKey = death.get(index).getNameKey();
      Optional<ScriptActions> actionsOpt = worldState.getScript().deathScript(nameKey);
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
