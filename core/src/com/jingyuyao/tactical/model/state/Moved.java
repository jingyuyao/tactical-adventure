package com.jingyuyao.tactical.model.state;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

/**
 * Can only perform actions after moving.
 */
public class Moved extends ControllingActionState {

  @Inject
  Moved(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      World world,
      @Assisted Cell cell) {
    super(modelBus, worldState, stateFactory, world, cell);
  }

  @Override
  public void select(Cell cell) {
    for (Ship ship : cell.ship().asSet()) {
      if (!getShip().equals(ship)) {
        rollback();
        if (ship.isControllable()) {
          goTo(getStateFactory().createMoving(cell, getWorld().getShipMovement(cell)));
        }
      }
    }
  }
}
