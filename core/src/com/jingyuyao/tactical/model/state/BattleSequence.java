package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.script.DeathEvent;
import com.jingyuyao.tactical.model.world.World;
import java.util.Iterator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class BattleSequence {

  private final ModelBus modelBus;
  private final World world;
  private final WorldState worldState;
  private final ScriptRunner scriptRunner;

  @Inject
  BattleSequence(ModelBus modelBus, World world, WorldState worldState, ScriptRunner scriptRunner) {
    this.modelBus = modelBus;
    this.world = world;
    this.worldState = worldState;
    this.scriptRunner = scriptRunner;
  }

  void start(final Battle battle, final Runnable done) {
    modelBus.post(new StartBattle(battle, new Promise(new Runnable() {
      @Override
      public void run() {
        triggerDeaths(battle.getDeaths().iterator(), done);
      }
    })));
  }

  private void triggerDeaths(final Iterator<Person> deaths, final Runnable done) {
    if (deaths.hasNext()) {
      DeathEvent deathEvent = new DeathEvent(deaths.next(), world);
      scriptRunner.triggerScripts(deathEvent, worldState.getScript())
          .done(new Runnable() {
            @Override
            public void run() {
              triggerDeaths(deaths, done);
            }
          });
    } else {
      done.run();
    }
  }
}
