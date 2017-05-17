package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.PilotResponse;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Retaliating extends BaseState {

  private final StateFactory stateFactory;
  private final Movements movements;
  private final World world;
  private final BattleSequence battleSequence;

  @Inject
  Retaliating(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      World world,
      BattleSequence battleSequence) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
    this.movements = movements;
    this.world = world;
    this.battleSequence = battleSequence;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.ENEMY));
    super.enter();
    retaliate(world.getShipSnapshot(), 0);
  }

  /**
   * Now we code like Racket!
   */
  private void retaliate(final ImmutableList<Cell> shipSnapshot, final int i) {
    if (i == shipSnapshot.size()) {
      getTurn().advance();
      post(new Save());
      branchTo(stateFactory.createStartTurn());
      return;
    }

    final Runnable next = new Runnable() {
      @Override
      public void run() {
        retaliate(shipSnapshot, i + 1);
      }
    };

    Cell cell = shipSnapshot.get(i);
    Optional<Ship> shipOpt = cell.ship();
    if (shipOpt.isPresent() && shipOpt.get().getAllegiance().equals(Allegiance.ENEMY)) {
      Ship enemy = shipOpt.get();
      post(new ActivatedEnemy(enemy));
      handleMoving(enemy.getAutoPilotResponse(cell, movements), next);
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
