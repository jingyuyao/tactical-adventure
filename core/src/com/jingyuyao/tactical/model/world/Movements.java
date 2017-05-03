package com.jingyuyao.tactical.model.world;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains methods regarding different kind of movements from a given spot in the world.
 */
@Singleton
public class Movements {

  private final World world;

  @Inject
  Movements(World world) {
    this.world = world;
  }

  public Optional<Cell> getNeighbor(Cell from, Direction direction) {
    return world.getCell(from.getCoordinate().offsetBy(direction));
  }

  public List<Cell> getNeighbors(final Cell from) {
    List<Cell> neighbors = new ArrayList<>(4);
    for (Direction direction : Direction.values()) {
      Optional<Cell> neighborOpt = getNeighbor(from, direction);
      if (neighborOpt.isPresent()) {
        neighbors.add(neighborOpt.get());
      }
    }
    return neighbors;
  }

  /**
   * Create a {@link Movement} for the {@link Character} contained in the given {@link Cell}
   */
  public Movement distanceFrom(Cell cell) {
    Preconditions.checkArgument(cell.character().isPresent());
    Character character = cell.character().get();
    return new Movement(
        distanceFrom(
            cell,
            character.getMoveDistance(),
            createEdgeCostFunction(character)));
  }

  /**
   * Create {@link Movement} starting at cell spanning a set distance. Each cell costs one to move.
   */
  public Movement distanceFrom(Cell cell, int distance) {
    return new Movement(distanceFrom(cell, distance, new ConstWeight()));
  }

  Function<Cell, Integer> createEdgeCostFunction(final Character character) {
    return new Function<Cell, Integer>() {
      @Override
      public Integer apply(Cell input) {
        Terrain terrain = input.getTerrain();
        if (input.character().isPresent() || !terrain.canHold(character)) {
          return Dijkstra.NO_EDGE;
        }
        return terrain.getMovementPenalty();
      }
    };
  }

  private Graph<Cell> distanceFrom(
      Cell startingCell, int distance, Function<Cell, Integer> edgeCostFunction) {
    return Dijkstra.minPathSearch(startingCell, distance, edgeCostFunction,
        new Function<Cell, List<Cell>>() {
          @Override
          public List<Cell> apply(Cell input) {
            return getNeighbors(input);
          }
        });
  }

  private static class ConstWeight implements Function<Cell, Integer> {

    @Override
    public Integer apply(Cell input) {
      return 1;
    }
  }
}
