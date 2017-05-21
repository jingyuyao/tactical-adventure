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

/**
 * Note: this is not a {@link TurnScriptState} is to mirror the fact that {@link Waiting} is not a
 * {@link TurnScriptState}. All the scripts for {@link TurnStage#ENEMY} should happen on {@link
 * TurnStage#END} instead.
 */
public class Retaliating extends BaseState {

  private final World world;
  private final StateFactory stateFactory;
  private final BattleSequence battleSequence;

  @Inject
  Retaliating(
      ModelBus modelBus,
      WorldState worldState,
      World world,
      StateFactory stateFactory,
      BattleSequence battleSequence) {
    super(modelBus, worldState);
    this.world = world;
    this.stateFactory = stateFactory;
    this.battleSequence = battleSequence;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.ENEMY));
    super.enter();
    retaliate(world.getShipSnapshot().entrySet().iterator());
  }

  private void retaliate(final Iterator<Entry<Cell, Ship>> shipsIterator) {
    if (!shipsIterator.hasNext()) {
      advanceTurn();
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
      handleMoving(ship.getAutoPilotResponse(world, cell), next);
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
