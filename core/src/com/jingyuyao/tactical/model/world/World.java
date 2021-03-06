package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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
import com.jingyuyao.tactical.model.world.WorldModule.InactiveShips;
import com.jingyuyao.tactical.model.world.WorldModule.WorldCells;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class World implements GetNeighbors {

  private final ModelBus modelBus;
  private final Dijkstra dijkstra;
  private final Map<Coordinate, Cell> worldCells;
  private final List<Ship> inactiveShips;
  private int level;
  private int maxHeight;
  private int maxWidth;

  @Inject
  World(
      ModelBus modelBus,
      Dijkstra dijkstra,
      @WorldCells Map<Coordinate, Cell> worldCells,
      @InactiveShips List<Ship> inactiveShips) {
    this.modelBus = modelBus;
    this.dijkstra = dijkstra;
    this.worldCells = worldCells;
    this.inactiveShips = inactiveShips;
  }

  public void initialize(int level, List<Cell> cells, List<Ship> inactiveShips) {
    for (Cell cell : cells) {
      Coordinate coordinate = cell.getCoordinate();
      if (worldCells.containsKey(coordinate)) {
        throw new IllegalArgumentException("Duplicated cell coordinate detected");
      }
      worldCells.put(coordinate, cell);
      // index is zero based
      maxWidth = Math.max(maxWidth, coordinate.getX() + 1);
      maxHeight = Math.max(maxHeight, coordinate.getY() + 1);
    }
    this.inactiveShips.addAll(inactiveShips);
    this.level = level;
    modelBus.post(new WorldLoaded(this));
  }

  public void reset() {
    worldCells.clear();
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

  public List<Cell> getWorldCells() {
    return new ArrayList<>(worldCells.values());
  }

  public Optional<Cell> cell(int x, int y) {
    return cell(new Coordinate(x, y));
  }

  public Optional<Cell> cell(Coordinate coordinate) {
    return Optional.fromNullable(worldCells.get(coordinate));
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
    return getShipMovement(cell, 1);
  }

  /**
   * Create a {@link Movement} for the {@link Ship} contained in the given {@link Cell} with
   * the given {@code distanceMultiplier} applied to the ship's travel distance.
   */
  public Movement getShipMovement(Cell cell, float distanceMultiplier) {
    Preconditions.checkArgument(cell.ship().isPresent());
    return createMovement(
        new TerrainCost(), cell, (int) (cell.ship().get().getMoveDistance() * distanceMultiplier));
  }

  /**
   * Create a {@link Movement} from cell spanning a set distance with a cost of one per cell.
   */
  public Movement getUnimpededMovement(Cell cell, int distance) {
    return createMovement(new OneCost(), cell, distance);
  }

  /**
   * Return a snapshot of the active ships.
   */
  public Map<Cell, Ship> getActiveShips() {
    Map<Cell, Ship> shipMap = new HashMap<>();
    for (Cell cell : worldCells.values()) {
      for (Ship ship : cell.ship().asSet()) {
        shipMap.put(cell, ship);
      }
    }
    return shipMap;
  }

  /**
   * Return a snapshot of the inactive ships.
   */
  public List<Ship> getInactiveShips() {
    return new ArrayList<>(inactiveShips);
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
   * Activates the ships from given group. The spawn cells should not contain any ships.
   */
  public void activateGroup(ShipGroup group, List<Cell> spawns) {
    int spawnIndex = 0;
    for (Iterator<Ship> shipIterator = inactiveShips.iterator();
        shipIterator.hasNext() && spawnIndex < spawns.size(); ) {
      Ship ship = shipIterator.next();
      if (ship.inGroup(group)) {
        spawnShip(spawns.get(spawnIndex), ship);
        spawnIndex++;
        shipIterator.remove();
      }
    }
  }

  /**
   * Deactivates the ships from the given group.
   */
  public void deactivateGroup(ShipGroup group) {
    for (Cell cell : worldCells.values()) {
      for (Ship ship : cell.ship().asSet()) {
        if (ship.inGroup(group)) {
          inactiveShips.add(removeShip(cell));
        }
      }
    }
  }

  /**
   * Make all the ships that belongs to {@link ShipGroup#PLAYER} controllable.
   */
  public void makeAllPlayerShipsControllable() {
    for (Cell cell : worldCells.values()) {
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
