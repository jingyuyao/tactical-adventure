package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.InstantMoveShip;
import com.jingyuyao.tactical.model.event.MoveShip;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.RemoveShip;
import com.jingyuyao.tactical.model.event.SpawnShip;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.WorldModule.BackingCellMap;
import com.jingyuyao.tactical.model.world.WorldModule.BackingInactiveList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class World implements GetNeighbors {

  private final ModelBus modelBus;
  private final Dijkstra dijkstra;
  private final Map<Coordinate, Cell> cellMap;
  private final List<Ship> inactiveShips;
  private int level;
  private int maxHeight;
  private int maxWidth;

  @Inject
  World(
      ModelBus modelBus,
      Dijkstra dijkstra,
      @BackingCellMap Map<Coordinate, Cell> cellMap,
      @BackingInactiveList List<Ship> inactiveShips) {
    this.modelBus = modelBus;
    this.dijkstra = dijkstra;
    this.cellMap = cellMap;
    this.inactiveShips = inactiveShips;
  }

  public void initialize(
      int level,
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Ship> shipMap,
      List<Ship> inactiveShips) {
    for (Entry<Coordinate, Terrain> entry : terrainMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Duplicated terrain detected");
      }
      Cell cell = new Cell(coordinate, entry.getValue());
      cellMap.put(coordinate, cell);
      // index is zero based
      maxWidth = Math.max(maxWidth, coordinate.getX() + 1);
      maxHeight = Math.max(maxHeight, coordinate.getY() + 1);
    }
    for (Entry<Coordinate, Ship> entry : shipMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (!cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Ship not on a terrain");
      }
      Cell cell = cellMap.get(coordinate);
      if (cell.ship().isPresent()) {
        throw new IllegalArgumentException("Ship occupying same space as another");
      }
      Ship ship = entry.getValue();
      if (cell.getTerrain().canHold(ship)) {
        spawnShip(cell, ship);
      } else {
        throw new IllegalArgumentException(ship + " can't be on " + cell.getTerrain());
      }
    }
    this.level = level;
    this.inactiveShips.addAll(inactiveShips);
    modelBus.post(new WorldLoaded(this));
  }

  public void reset() {
    cellMap.clear();
    inactiveShips.clear();
    level = 0;
    maxWidth = 0;
    maxHeight = 0;
    modelBus.post(new WorldReset(this));
  }

  public int getLevel() {
    return level;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public Optional<Cell> cell(int x, int y) {
    return cell(new Coordinate(x, y));
  }

  public Optional<Cell> cell(Coordinate coordinate) {
    return Optional.fromNullable(cellMap.get(coordinate));
  }

  @Override
  public Iterable<Cell> getNeighbors(Cell from) {
    List<Cell> neighbors = new ArrayList<>(Direction.values().length);
    for (Direction direction : Direction.values()) {
      Optional<Cell> neighborOpt = neighbor(from, direction);
      if (neighborOpt.isPresent()) {
        neighbors.add(neighborOpt.get());
      }
    }
    return neighbors;
  }

  public Optional<Cell> neighbor(Cell from, Direction direction) {
    return cell(from.getCoordinate().offsetBy(direction));
  }

  /**
   * Create a {@link Movement} for the {@link Ship} contained in the given {@link Cell}
   */
  public Movement getShipMovement(Cell cell) {
    Preconditions.checkArgument(cell.ship().isPresent());
    Ship ship = cell.ship().get();
    return createMovement(new ShipCost(ship), cell, ship.getMoveDistance());
  }

  /**
   * Create a {@link Movement} from cell spanning a set distance with a cost of one per cell.
   */
  public Movement getUnimpededMovement(Cell cell, int distance) {
    return createMovement(new OneCost(), cell, distance);
  }

  /**
   * Return a snapshot of all the ships in the world.
   */
  public ImmutableMap<Cell, Ship> getShipSnapshot() {
    ImmutableMap.Builder<Cell, Ship> builder = new ImmutableMap.Builder<>();
    for (Cell cell : cellMap.values()) {
      for (Ship ship : cell.ship().asSet()) {
        builder.put(cell, ship);
      }
    }
    return builder.build();
  }

  public ImmutableList<Ship> getInactiveShips() {
    return ImmutableList.copyOf(inactiveShips);
  }

  /**
   * Spawn a ship at the given cell. Fires {@link SpawnShip}.
   */
  public void spawnShip(Cell cell, Ship ship) {
    cell.addShip(ship);
    modelBus.post(new SpawnShip(cell));
  }

  /**
   * Remove the ship at the given cell. Fires {@link RemoveShip}. <p>{@code cell} must have a ship.
   *
   * @return the removed ship.
   */
  public Ship removeShip(Cell cell) {
    Ship removed = cell.removeShip();
    modelBus.post(new RemoveShip(removed));
    return removed;
  }

  /**
   * Instantly moves the ship between cells. Fires {@link InstantMoveShip} if a ship was moved.
   * <p>{@code orig} must have a ship. {@code dest} must not have a ship unless it is the same as
   * {@code orig}.
   */
  public void moveShip(Cell orig, Cell dest) {
    for (Ship ship : orig.moveShip(dest).asSet()) {
      modelBus.post(new InstantMoveShip(ship, dest));
    }
  }

  /**
   * Moves the ship along a path. Fires {@link MoveShip} if a ship was moved. <p>{@link
   * Path#getOrigin()} must have a ship. {@link Path#getDestination()} must not have a ship unless
   * it is the same as {@link Path#getOrigin()}.
   *
   * @return a promise that is completed when {@link MoveShip} is completed.
   */
  public Promise moveShip(Path path) {
    Optional<Ship> moved = path.getOrigin().moveShip(path.getDestination());
    if (moved.isPresent()) {
      Promise promise = new Promise();
      modelBus.post(new MoveShip(moved.get(), path, promise));
      return promise;
    } else {
      return Promise.immediate();
    }
  }

  /**
   * Move a ship from the inactive list to the specified cell.
   */
  public void activateShip(Cell cell, Ship inactiveShip) {
    Preconditions.checkArgument(inactiveShips.remove(inactiveShip));
    spawnShip(cell, inactiveShip);
  }

  /**
   * Moves the ship from the given cell to the inactive list.
   */
  public void deactivateShip(Cell cell) {
    inactiveShips.add(removeShip(cell));
  }

  /**
   * Make all the ships that belongs to {@link ShipGroup#PLAYER} controllable.
   */
  public void makeAllPlayerShipsControllable() {
    for (Cell cell : cellMap.values()) {
      for (Ship ship : cell.ship().asSet()) {
        if (ship.inGroup(ShipGroup.PLAYER)) {
          ship.setControllable(true);
        }
      }
    }
  }

  private Movement createMovement(GetEdgeCost getEdgeCost, Cell startingCell, int distance) {
    return new Movement(dijkstra.minPathSearch(this, getEdgeCost, startingCell, distance));
  }
}
