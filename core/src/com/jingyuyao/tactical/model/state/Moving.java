package com.jingyuyao.tactical.model.state;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Moving extends ControllingActionState {

  private final Movement movement;
  private Cell prevMove;

  @Inject
  Moving(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      World world,
      @Assisted Cell cell,
      @Assisted Movement movement) {
    super(modelBus, worldState, stateFactory, world, cell);
    this.movement = movement;
  }

  @Override
  public void canceled() {
    if (prevMove != null) {
      prevMove.instantMoveShip(movement.getStartingCell());
    }
  }

  @Override
  public void select(final Cell cell) {
    if (cell.ship().isPresent()) {
      Ship ship = cell.ship().get();
      if (!getShip().equals(ship)) {
        rollback();
        if (ship.isControllable()) {
          goTo(getStateFactory().createMoving(cell, getWorld().getShipMovement(cell)));
        }
      }
    } else {
      if (movement.canMoveTo(cell)) {
        Path path = movement.pathTo(cell);
        prevMove = cell;
        goTo(getStateFactory().createTransition());
        movement.getStartingCell().moveShip(path).done(new Runnable() {
          @Override
          public void run() {
            goTo(getStateFactory().createMoved(cell));
          }
        });
      }
    }
  }

  public Movement getMovement() {
    return movement;
  }
}
