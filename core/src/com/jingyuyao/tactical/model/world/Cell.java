package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ship.Ship;
import java.io.Serializable;

public class Cell implements Serializable {

  private Coordinate coordinate;
  private Terrain terrain;
  private Ship ship;

  Cell() {
  }

  public Cell(Coordinate coordinate, Terrain terrain) {
    this.coordinate = coordinate;
    this.terrain = terrain;
  }

  public Cell(Coordinate coordinate, Terrain terrain, Ship ship) {
    Preconditions.checkArgument(terrain.canHoldShip());
    this.coordinate = coordinate;
    this.terrain = terrain;
    this.ship = ship;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public Optional<Ship> ship() {
    return Optional.fromNullable(ship);
  }

  /**
   * Add a ship to this cell. This cell must not contain a ship and the terrain must be able to
   * hold the ship.
   */
  void addShip(Ship newShip) {
    Preconditions.checkState(ship == null);
    Preconditions.checkArgument(terrain.canHoldShip());
    ship = newShip;
  }

  /**
   * Remove the ship contained by this cell. This cell must have a ship.
   */
  Ship removeShip() {
    Preconditions.checkState(ship != null);
    Ship removed = ship;
    ship = null;
    return removed;
  }

  /**
   * Move the ship in this cell to dest. This cell must have a ship. Dest must not have a ship
   * unless it is this cell, in which case no move is performed.
   *
   * @return {@link Optional#absent()} if no move is performed, otherwise return an optional
   * containing the moved ship
   */
  Optional<Ship> moveShip(Cell dest) {
    Preconditions.checkState(ship != null);
    if (equals(dest)) {
      return Optional.absent();
    }
    Ship moved = ship;
    dest.addShip(ship);
    ship = null;
    return Optional.of(moved);
  }
}
