package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.script.DeathEvent;
import com.jingyuyao.tactical.model.world.Cell;
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
        triggerDeaths(battle.getDeadCells().iterator(), done);
      }
    })));
  }

  private void triggerDeaths(final Iterator<Cell> deadCells, final Runnable done) {
    if (deadCells.hasNext()) {
      Cell deadCell = deadCells.next();
      Preconditions.checkArgument(deadCell.ship().isPresent());
      world.removeShip(deadCell);
      for (Person crew : deadCell.ship().get().getCrew()) {
        DeathEvent deathEvent = new DeathEvent(crew, world);
        scriptRunner.triggerScripts(deathEvent, worldState.getScript())
            .done(new Runnable() {
              @Override
              public void run() {
                triggerDeaths(deadCells, done);
              }
            });
      }
    } else {
      done.run();
    }
  }
}
