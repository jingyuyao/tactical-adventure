package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.script.ShipDestroyed;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
        final List<Ship> deadShips = new ArrayList<>(battle.getDeadCells().size());
        // Remove ships first to ensure the state of the world is valid before any scripting happens
        // so even if some weird shit went down in the scripts our world wouldn't have any dead ships.
        for (Cell cell : battle.getDeadCells()) {
          deadShips.add(world.removeShip(cell));
        }
        // Sort player ships before other type of ships so you can't kamikaze ships attached to a lose
        // condition and circumvent losing.
        Collections.sort(deadShips, new Comparator<Ship>() {
          @Override
          public int compare(Ship s1, Ship s2) {
            int s1g = s1.inGroup(ShipGroup.PLAYER) ? 0 : 1;
            int s2g = s2.inGroup(ShipGroup.PLAYER) ? 0 : 1;
            return s1g - s2g;
          }
        });
        triggerDeadShipScripts(deadShips.iterator(), done);
      }
    })));
  }

  private void triggerDeadShipScripts(final Iterator<Ship> deadShips, final Runnable done) {
    if (deadShips.hasNext()) {
      Ship destroyed = deadShips.next();
      ShipDestroyed shipDestroyed = new ShipDestroyed(destroyed, world);
      scriptRunner.triggerScripts(shipDestroyed, worldState.getScript())
          .done(new Runnable() {
            @Override
            public void run() {
              triggerDeadShipScripts(deadShips, done);
            }
          });
    } else {
      done.run();
    }
  }
}
