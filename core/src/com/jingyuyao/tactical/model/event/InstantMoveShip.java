package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.world.Cell;

public class InstantMoveShip {

  private final Ship ship;
  private final Cell destination;

  public InstantMoveShip(Ship ship, Cell destination) {
    this.ship = ship;
    this.destination = destination;
  }

  public Ship getShip() {
    return ship;
  }

  public Cell getDestination() {
    return destination;
  }
}
