package com.jingyuyao.tactical.model.world;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.inject.Singleton;

/**
 * A Dijkstra's implementation with interfaces to use it.
 */
@Singleton
class Dijkstra {

  /**
   * Creates a directed, acyclic graph starting at {@code startingCell} that contains all
   * reachable nodes whose total path cost (sum of all edge weights from the start to the current
   * node) is less or equal to {@code distance}. Inbound edge costs comes from {@code edgeCostFunc}.
   *
   * @param neighborsFunc a function to return the neighbors of a given cell
   * @param edgeCostFunc a function to obtain the incoming edge cost for a particular {@link Cell}.
   * All incoming edge cost for a {@link Cell} is assumed to be the same.
   * @param distance Maximum distance for the path between initial location to any other object
   */
  ValueGraph<Cell, Integer> minPathSearch(
      GetNeighbors neighborsFunc, GetEdgeCost edgeCostFunc, Cell startingCell, int distance) {
    MutableValueGraph<Cell, Integer> graph =
        ValueGraphBuilder
            .directed()
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

      for (Cell neighborCell : neighborsFunc.getNeighbors(minCell)) {
        if (processedCells.contains(neighborCell)) {
          continue;
        }

        int edgeCost = edgeCostFunc.getEdgeCost(neighborCell);
        if (edgeCost == GetEdgeCost.NO_EDGE) {
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

  interface GetEdgeCost {

    int NO_EDGE = -1;

    int getEdgeCost(Cell cell);
  }

  interface GetNeighbors {

    Iterable<Cell> getNeighbors(Cell cell);
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
