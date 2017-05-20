package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class BattleSequence {

  private final ModelBus modelBus;
  private final ScriptRunner scriptRunner;

  @Inject
  BattleSequence(ModelBus modelBus, ScriptRunner scriptRunner) {
    this.modelBus = modelBus;
    this.scriptRunner = scriptRunner;
  }

  void start(final Battle battle, final Runnable done) {
    modelBus.post(new StartBattle(battle, new Promise(new Runnable() {
      @Override
      public void run() {
        scriptRunner.triggerScripts(done);
      }
    })));
  }
}
