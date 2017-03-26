package com.jingyuyao.tactical.model.world;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Contains methods to produce graphs from the current terrains.
 */
@Singleton
public class Movements {

  static final int BLOCKED = -1;

  private final World world;

  @Inject
  Movements(World world) {
    this.world = world;
  }

  /**
   * Create a {@link Movement} for the {@link Character} in a {@link Cell}.
   */
  public Movement distanceFrom(Cell cell) {
    Preconditions.checkArgument(cell.hasCharacter());
    Character character = cell.getCharacter();
    return new Movement(
        distanceFrom(
            cell,
            character.getMoveDistance(),
            createEdgeCostFunction(character)));
  }

  /**
   * Creates a directed, acyclic graph starting at {@code startingCell} that contains all
   * reachable nodes whose total path cost (sum of all edge weights from the start to the current
   * node) is less or equal to {@code distance}. Inbound edge costs comes from {@code
   * edgeCosFunction}.
   *
   * @param distance Maximum distance for the path between initial location to any other object
   * @param edgeCostFunction The function to obtain the incoming edge cost for a particular {@link
   * Cell}. All incoming edge cost for a {@link Cell} is assumed to be the same.
   */
  public ValueGraph<Cell, Integer> distanceFrom(
      Cell startingCell, int distance, Function<Cell, Integer> edgeCostFunction) {
    MutableValueGraph<Cell, Integer> graph =
        ValueGraphBuilder.directed()
            .allowsSelfLoops(false)
            .nodeOrder(ElementOrder.insertion())
            .build();
    Set<Cell> processedCells = new HashSet<>();
    Map<Cell, Integer> pathCostMap = new HashMap<>();
    Queue<ValueNode<Cell>> minNodeQueue = new PriorityQueue<>();

    pathCostMap.put(startingCell, 0);
    graph.addNode(startingCell);
    minNodeQueue.add(new ValueNode<>(startingCell, 0));

    // Dijkstra's algorithm
    while (!minNodeQueue.isEmpty()) {
      ValueNode<Cell> minNode = minNodeQueue.poll();
      Cell minCell = minNode.getObject();
      processedCells.add(minCell);

      for (Cell neighborCell : world.getNeighbors(minCell)) {
        if (processedCells.contains(neighborCell)) {
          continue;
        }

        Integer edgeCost = edgeCostFunction.apply(neighborCell);
        Preconditions.checkNotNull(edgeCost);
        if (edgeCost == BLOCKED) {
          continue;
        }

        int pathCost = minNode.getValue() + edgeCost;
        if (pathCost > distance) {
          continue;
        }

        ValueNode<Cell> neighborNode = new ValueNode<>(neighborCell, pathCost);
        if (!pathCostMap.containsKey(neighborCell)) {
          graph.putEdgeValue(minCell, neighborCell, pathCost);
          pathCostMap.put(neighborCell, pathCost);
        } else if (pathCost < pathCostMap.get(neighborCell)) {
          // Remove neighbor from graph so that (minNode, neighbor) is the only edge
          graph.removeNode(neighborCell);
          graph.putEdgeValue(minCell, neighborCell, pathCost);
          // Adjust path cost of the current neighbor
          pathCostMap.put(neighborCell, pathCost);
          minNodeQueue.remove(neighborNode);
        }
        minNodeQueue.add(neighborNode);
      }
    }

    Preconditions.checkState(
        graph.predecessors(
            startingCell).isEmpty(), "Graph does not contain a terminating node");
    Preconditions.checkState(!Graphs.hasCycle(graph), "Cycle in distanceFrom");
    return graph;
  }

  Function<Cell, Integer> createEdgeCostFunction(final Character character) {
    return new Function<Cell, Integer>() {
      @Override
      public Integer apply(Cell input) {
        Terrain terrain = input.getTerrain();
        if (input.hasCharacter() || !terrain.canHold(character)) {
          return Movements.BLOCKED;
        }
        return terrain.getMovementPenalty();
      }
    };
  }

  /**
   * Store an object with an integer value. Identity of this object is based off {@link #object}.
   * {@link #value} is not part of the identity. Used for {@link PriorityQueue} sorting.
   */
  private static class ValueNode<N> implements Comparable<ValueNode<N>> {

    private final N object;
    private final int value;

    ValueNode(N object, int value) {
      this.object = object;
      this.value = value;
    }

    N getObject() {
      return object;
    }

    int getValue() {
      return value;
    }

    @Override
    public int compareTo(ValueNode<N> other) {
      return value - other.value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ValueNode<?> valueNode = (ValueNode<?>) o;
      return Objects.equal(object, valueNode.object);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(object);
    }
  }
}
