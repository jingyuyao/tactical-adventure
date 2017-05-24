package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.script.ShipDestroyed;
import com.jingyuyao.tactical.model.ship.Ship;
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
        removeDeadShips(battle.getDeadCells().iterator(), done);
      }
    })));
  }

  private void removeDeadShips(final Iterator<Cell> deadCells, final Runnable done) {
    if (deadCells.hasNext()) {
      Cell deadCell = deadCells.next();
      Ship destroyed = world.removeShip(deadCell);
      ShipDestroyed shipDestroyed = new ShipDestroyed(destroyed, world);
      scriptRunner.triggerScripts(shipDestroyed, worldState.getScript())
          .done(new Runnable() {
            @Override
            public void run() {
              removeDeadShips(deadCells, done);
            }
          });
    } else {
      done.run();
    }
  }
}
