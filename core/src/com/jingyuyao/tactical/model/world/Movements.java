package com.jingyuyao.tactical.model.world;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.ship.Ship;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains methods to travel the world from a given cell. Think of {@link World} as a data
 * container and this class contains the logic to move around in it.
 */
@Singleton
public class Movements implements GetNeighbors {

  private final World world;
  private final Dijkstra dijkstra;

  @Inject
  Movements(World world, Dijkstra dijkstra) {
    this.world = world;
    this.dijkstra = dijkstra;
  }

  @Override
  public Iterable<Cell> getNeighbors(final Cell from) {
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
    return world.cell(from.getCoordinate().offsetBy(direction));
  }

  /**
   * Create a {@link Movement} for the {@link Ship} contained in the given {@link Cell}
   */
  public Movement distanceFrom(Cell cell) {
    Preconditions.checkArgument(cell.ship().isPresent());
    Ship ship = cell.ship().get();
    return new Movement(
        distanceFrom(new ShipCost(ship), cell, ship.getMoveDistance()));
  }

  /**
   * Create a {@link Movement} from cell spanning a set distance with a cost of one per cell.
   */
  public Movement distanceFrom(Cell cell, int distance) {
    return new Movement(distanceFrom(new OneCost(), cell, distance));
  }

  private Graph<Cell> distanceFrom(GetEdgeCost getEdgeCost, Cell startingCell, int distance) {
    return dijkstra.minPathSearch(this, getEdgeCost, startingCell, distance);
  }
}
