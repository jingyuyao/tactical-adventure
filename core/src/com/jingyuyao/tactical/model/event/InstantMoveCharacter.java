package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.character.Ship;
import com.jingyuyao.tactical.model.world.Cell;

public class InstantMoveCharacter {

  private final Ship ship;
  private final Cell destination;

  public InstantMoveCharacter(Ship ship, Cell destination) {
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
