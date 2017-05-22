package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

public class Cell {

  private final ModelBus modelBus;
  private final Coordinate coordinate;
  private final Terrain terrain;
  private Ship ship;

  @Inject
  Cell(ModelBus modelBus, @Assisted Coordinate coordinate, @Assisted Terrain terrain) {
    this.modelBus = modelBus;
    this.coordinate = coordinate;
    this.terrain = terrain;
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
    Preconditions.checkArgument(terrain.canHold(newShip));
    ship = newShip;
  }

  /**
   * Remove the ship contained by this cell. This cell must have a ship.
   */
  // TODO: temp name
  Ship delShip() {
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

  public void spawnShip(Ship ship) {
    // TODO: handle cases where there is already a ship at the coordinate gracefully
    Preconditions.checkState(this.ship == null);
    Preconditions.checkNotNull(ship);

    this.ship = ship;
    modelBus.post(new SpawnShip(this));
  }

  public void removeShip() {
    Preconditions.checkState(ship != null);

    modelBus.post(new RemoveShip(ship));
    this.ship = null;
  }

  public void instantMoveShip(Cell destination) {
    Preconditions.checkState(ship != null);

    if (destination.equals(this)) {
      return;
    }

    Preconditions.checkArgument(destination.ship == null);

    modelBus.post(new InstantMoveShip(ship, destination));
    destination.ship = ship;
    ship = null;
  }

  public Promise moveShip(Path path) {
    Preconditions.checkState(ship != null);
    Preconditions.checkArgument(path.getOrigin().equals(this));

    Cell destination = path.getDestination();
    if (destination.equals(this)) {
      return Promise.immediate();
    }

    Preconditions.checkArgument(destination.ship == null);

    Promise promise = new Promise();
    modelBus.post(new MoveShip(ship, path, promise));
    path.getDestination().ship = ship;
    ship = null;
    return promise;
  }
}
