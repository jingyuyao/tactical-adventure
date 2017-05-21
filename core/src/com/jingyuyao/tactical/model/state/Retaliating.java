package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ActivatedShip;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.PilotResponse;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.inject.Inject;

public class Retaliating extends TurnState {

  private final StateFactory stateFactory;
  private final BattleSequence battleSequence;

  @Inject
  Retaliating(
      ModelBus modelBus,
      WorldState worldState,
      World world,
      ScriptRunner scriptRunner,
      StateFactory stateFactory,
      BattleSequence battleSequence) {
    super(modelBus, worldState, world, scriptRunner);
    this.stateFactory = stateFactory;
    this.battleSequence = battleSequence;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.ENEMY));
    super.enter();
  }

  @Override
  void scriptDone() {
    retaliate(getWorld().getShipSnapshot().entrySet().iterator());
  }

  private void retaliate(final Iterator<Entry<Cell, Ship>> shipsIterator) {
    if (!shipsIterator.hasNext()) {
      getTurn().advance();
      post(new Save());
      branchTo(stateFactory.createStartTurn());
      return;
    }

    Runnable next = new Runnable() {
      @Override
      public void run() {
        retaliate(shipsIterator);
      }
    };

    Entry<Cell, Ship> entry = shipsIterator.next();
    Cell cell = entry.getKey();
    Ship ship = entry.getValue();
    if (ship.inGroup(ShipGroup.ENEMY)) {
      post(new ActivatedShip(ship));
      handleMoving(ship.getAutoPilotResponse(getWorld(), cell), next);
    } else {
      next.run();
    }
  }

  private void handleMoving(final PilotResponse pilotResponse, final Runnable next) {
    final Optional<Path> pathOpt = pilotResponse.path();
    if (pathOpt.isPresent()) {
      Path path = pathOpt.get();
      path.getOrigin().moveShip(path).done(new Runnable() {
        @Override
        public void run() {
          handleBattle(pilotResponse, next);
        }
      });
    } else {
      handleBattle(pilotResponse, next);
    }
  }

  private void handleBattle(PilotResponse pilotResponse, Runnable next) {
    if (pilotResponse.battle().isPresent()) {
      battleSequence.start(pilotResponse.battle().get(), next);
    } else {
      next.run();
    }
  }
}
