package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;

public class ControllingState extends BaseState {

  private final StateFactory stateFactory;
  private final Cell cell;
  private final Ship ship;

  ControllingState(
      ModelBus modelBus, WorldState worldState, StateFactory stateFactory, Cell cell) {
    super(modelBus, worldState);
    Preconditions.checkArgument(cell.ship().isPresent());
    Preconditions.checkArgument(cell.ship().get().isControllable());
    this.stateFactory = stateFactory;
    this.cell = cell;
    this.ship = cell.ship().get();
  }

  /**
   * The {@link Cell} being controlled.
   */
  public Cell getCell() {
    return cell;
  }

  /**
   * The {@link Ship} being controlled.
   */
  public Ship getShip() {
    return ship;
  }

  StateFactory getStateFactory() {
    return stateFactory;
  }

  void finish() {
    ship.setControllable(false);
    branchTo(stateFactory.createTransition());
    post(new Save(new Promise(new Runnable() {
      @Override
      public void run() {
        branchTo(stateFactory.createWaiting());
      }
    })));
  }
}
