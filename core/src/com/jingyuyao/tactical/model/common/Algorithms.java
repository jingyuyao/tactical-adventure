package com.jingyuyao.tactical.model.common;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graph;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.jingyuyao.tactical.model.map.Terrains;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Functions should return immutable objects.
 */
@Singleton
public class Algorithms {

  public static final int NO_EDGE = -1;

  private final Terrains terrains;

  @Inject
  Algorithms(Terrains terrains) {
    this.terrains = terrains;
  }

  /**
   * Creates a directed, acyclic graph starting at {@code startingCoordinate} in {@code
   * coordinateGrid} that contains all reachable nodes whose total path cost (sum of all edge
   * weights from the start to the current node) is less or equal to {@code maxPathCost}. Inbound
   * edge costs comes from {@code edgeCostMap}.
   *
   * @param edgeCostFunction The function to obtain the incoming edge cost for a particular {@link
   * Coordinate}. All incoming edge cost for a {@link Coordinate} is assumed to be the same.
   * @param maxPathCost Maximum cost for the path between initial location to any other object
   */
  public ValueGraph<Coordinate, Integer> distanceFromGraph(
      Function<Coordinate, Integer> edgeCostFunction,
      Coordinate startingCoordinate,
      int maxPathCost) {
    MutableValueGraph<Coordinate, Integer> graph =
        ValueGraphBuilder.directed()
            .allowsSelfLoops(false)
            .nodeOrder(ElementOrder.insertion())
            .build();
    Set<Coordinate> processedCoordinates = new HashSet<Coordinate>();
    Map<Coordinate, Integer> pathCostMap = new HashMap<Coordinate, Integer>();
    Queue<ValueNode<Coordinate>> minNodeQueue = new PriorityQueue<ValueNode<Coordinate>>();

    pathCostMap.put(startingCoordinate, 0);
    minNodeQueue.add(new ValueNode<Coordinate>(startingCoordinate, 0));

    // Dijkstra's algorithm
    while (!minNodeQueue.isEmpty()) {
      ValueNode<Coordinate> minNode = minNodeQueue.poll();
      Coordinate minCoordinate = minNode.getObject();
      processedCoordinates.add(minCoordinate);

      for (Coordinate neighbor : getNeighbors(minCoordinate)) {
        if (processedCoordinates.contains(neighbor)) {
          continue;
        }

        Integer edgeCost = edgeCostFunction.apply(neighbor);
        Preconditions.checkNotNull(edgeCost);
        if (edgeCost == NO_EDGE) {
          continue;
        }

        int pathCost = minNode.getValue() + edgeCost;
        if (pathCost > maxPathCost) {
          continue;
        }

        ValueNode<Coordinate> neighborNode = new ValueNode<Coordinate>(neighbor, pathCost);
        if (!pathCostMap.containsKey(neighbor)) {
          graph.putEdgeValue(minCoordinate, neighbor, pathCost);
          pathCostMap.put(neighbor, pathCost);
        } else if (pathCost < pathCostMap.get(neighbor)) {
          // Remove neighbor from graph so that (minNode, neighbor) is the only edge
          graph.removeNode(neighbor);
          graph.putEdgeValue(minCoordinate, neighbor, pathCost);
          // Adjust path cost of the current neighbor
          pathCostMap.put(neighbor, pathCost);
          minNodeQueue.remove(neighborNode);
        }
        minNodeQueue.add(neighborNode);
      }
    }

    Preconditions.checkState(graph.predecessors(startingCoordinate).isEmpty());
    Preconditions.checkState(!Graphs.hasCycle(graph), "Cycle in distanceFromGraph");
    return graph;
  }

  /**
   * Get the track from the starting {@link Coordinate} in the {@code graph} to {@code target}.
   * {@code target} must exist in the {@code graph} or an exception will be thrown.
   *
   * @param graph An directed acyclic graph
   * @param target The target node to find a path to
   * @return A path to {@code target} from the first node in the graph or an empty list if target is
   * not in the graph
   */
  public ImmutableList<Coordinate> getTrackTo(Graph<Coordinate> graph, Coordinate target) {
    Preconditions.checkArgument(graph.nodes().contains(target));

    ImmutableList.Builder<Coordinate> builder = new ImmutableList.Builder<Coordinate>();
    builder.add(target);

    Set<Coordinate> predecessors = graph.predecessors(target);
    while (predecessors.size() != 0) {
      Preconditions.checkState(
          predecessors.size() == 1, "getTrackTo encountered a node with multiple predecessors");
      Coordinate predecessor = predecessors.iterator().next();
      builder.add(predecessor);
      predecessors = graph.predecessors(predecessor);
    }

    return builder.build().reverse();
  }

  /**
   * Returns the in-bound neighbors of {@code from}.
   *
   * @return Randomized list of neighbors
   */
  private ImmutableList<Coordinate> getNeighbors(Coordinate from) {
    int x = from.getX();
    int y = from.getY();

    List<Coordinate> neighbors = new ArrayList<Coordinate>(4);

    if (x > 0) {
      neighbors.add(new Coordinate(x - 1, y));
    }
    if (x < terrains.getWidth() - 1) {
      neighbors.add(new Coordinate(x + 1, y));
    }
    if (y > 0) {
      neighbors.add(new Coordinate(x, y - 1));
    }
    if (y < terrains.getHeight() - 1) {
      neighbors.add(new Coordinate(x, y + 1));
    }

    Collections.shuffle(neighbors);
    return ImmutableList.copyOf(neighbors);
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
