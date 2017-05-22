package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.ModelBus;
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
  private final CellFactory cellFactory;
  private final Map<Coordinate, Cell> cellMap;
  private final List<Ship> inactiveShips;
  private int maxHeight;
  private int maxWidth;

  @Inject
  World(
      ModelBus modelBus,
      Dijkstra dijkstra,
      CellFactory cellFactory,
      @BackingCellMap Map<Coordinate, Cell> cellMap,
      @BackingInactiveList List<Ship> inactiveShips) {
    this.modelBus = modelBus;
    this.dijkstra = dijkstra;
    this.cellFactory = cellFactory;
    this.cellMap = cellMap;
    this.inactiveShips = inactiveShips;
  }

  public void initialize(
      Map<Coordinate, Terrain> terrainMap,
      Map<Coordinate, Ship> shipMap,
      List<Ship> inactiveShips) {
    for (Entry<Coordinate, Terrain> entry : terrainMap.entrySet()) {
      Coordinate coordinate = entry.getKey();
      if (cellMap.containsKey(coordinate)) {
        throw new IllegalArgumentException("Duplicated terrain detected");
      }
      Cell cell = cellFactory.create(coordinate, entry.getValue());
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
        cell.spawnShip(ship);
      } else {
        throw new IllegalArgumentException(ship + " can't be on " + cell.getTerrain());
      }
    }
    this.inactiveShips.addAll(inactiveShips);
    modelBus.post(new WorldLoaded(this));
  }

  public void reset() {
    cellMap.clear();
    inactiveShips.clear();
    maxWidth = 0;
    maxHeight = 0;
    modelBus.post(new WorldReset(this));
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
   * Move a ship from the inactive list to the specified cell.
   */
  public void activateShip(Cell cell, Ship inactiveShip) {
    Preconditions.checkArgument(inactiveShips.remove(inactiveShip));
    cell.spawnShip(inactiveShip);
  }

  /**
   * Moves the ship from the given cell to the inactive list.
   */
  public void deactivateShip(Cell cell) {
    Preconditions.checkArgument(cell.ship().isPresent());
    inactiveShips.add(cell.ship().get());
    cell.removeShip();
  }

  public void makeAllPlayerShipsControllable() {
    for (Ship ship : getShipSnapshot().values()) {
      if (ship.inGroup(ShipGroup.PLAYER)) {
        ship.setControllable(true);
      }
    }
  }

  private Movement createMovement(GetEdgeCost getEdgeCost, Cell startingCell, int distance) {
    return new Movement(dijkstra.minPathSearch(this, getEdgeCost, startingCell, distance));
  }
}
