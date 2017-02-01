package com.jingyuyao.tactical.model.map;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
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

  private final Characters characters;
  private final Terrains terrains;
  private final MovementFactory movementFactory;

  @Inject
  Movements(Characters characters, Terrains terrains, MovementFactory movementFactory) {
    this.characters = characters;
    this.terrains = terrains;
    this.movementFactory = movementFactory;
  }

  public Movement distanceFrom(Character character) {
    return movementFactory.create(
        distanceFrom(
            character.getCoordinate(),
            character.getMoveDistance(),
            createEdgeCostFunction(character)));
  }

  /**
   * Creates a directed, acyclic graph starting at {@code startingCoordinate} that contains all
   * reachable nodes whose total path cost (sum of all edge weights from the start to the current
   * node) is less or equal to {@code distance}. Inbound edge costs comes from {@code
   * edgeCosFunction}.
   *
   * @param distance Maximum distance for the path between initial location to any other object
   * @param edgeCostFunction The function to obtain the incoming edge cost for a particular {@link
   * Coordinate}. All incoming edge cost for a {@link Coordinate} is assumed to be the same.
   */
  public ValueGraph<Coordinate, Integer> distanceFrom(
      Coordinate startingCoordinate, int distance, Function<Terrain, Integer> edgeCostFunction) {
    MutableValueGraph<Coordinate, Integer> graph =
        ValueGraphBuilder.directed()
            .allowsSelfLoops(false)
            .nodeOrder(ElementOrder.insertion())
            .build();
    Set<Coordinate> processedCoordinates = new HashSet<Coordinate>();
    Map<Coordinate, Integer> pathCostMap = new HashMap<Coordinate, Integer>();
    Queue<ValueNode<Coordinate>> minNodeQueue = new PriorityQueue<ValueNode<Coordinate>>();

    pathCostMap.put(startingCoordinate, 0);
    graph.addNode(startingCoordinate);
    minNodeQueue.add(new ValueNode<Coordinate>(startingCoordinate, 0));

    // Dijkstra's algorithm
    while (!minNodeQueue.isEmpty()) {
      ValueNode<Coordinate> minNode = minNodeQueue.poll();
      Coordinate minCoordinate = minNode.getObject();
      processedCoordinates.add(minCoordinate);

      for (Terrain neighborTerrain : terrains.getNeighbors(minCoordinate)) {
        Coordinate neighborCoordinate = neighborTerrain.getCoordinate();
        if (processedCoordinates.contains(neighborCoordinate)) {
          continue;
        }

        Integer edgeCost = edgeCostFunction.apply(neighborTerrain);
        Preconditions.checkNotNull(edgeCost);
        if (edgeCost == BLOCKED) {
          continue;
        }

        int pathCost = minNode.getValue() + edgeCost;
        if (pathCost > distance) {
          continue;
        }

        ValueNode<Coordinate> neighborNode =
            new ValueNode<Coordinate>(neighborCoordinate, pathCost);
        if (!pathCostMap.containsKey(neighborCoordinate)) {
          graph.putEdgeValue(minCoordinate, neighborCoordinate, pathCost);
          pathCostMap.put(neighborCoordinate, pathCost);
        } else if (pathCost < pathCostMap.get(neighborCoordinate)) {
          // Remove neighbor from graph so that (minNode, neighbor) is the only edge
          graph.removeNode(neighborCoordinate);
          graph.putEdgeValue(minCoordinate, neighborCoordinate, pathCost);
          // Adjust path cost of the current neighbor
          pathCostMap.put(neighborCoordinate, pathCost);
          minNodeQueue.remove(neighborNode);
        }
        minNodeQueue.add(neighborNode);
      }
    }

    Preconditions.checkState(
        graph.predecessors(
            startingCoordinate).isEmpty(), "Graph does not contain a terminating node");
    Preconditions.checkState(!Graphs.hasCycle(graph), "Cycle in distanceFrom");
    return graph;
  }

  Function<Terrain, Integer> createEdgeCostFunction(final Character character) {
    final ImmutableSet<Coordinate> blockedCoordinates = getBlockedCoordinates();
    return new Function<Terrain, Integer>() {
      @Override
      public Integer apply(Terrain input) {
        if (blockedCoordinates.contains(input.getCoordinate()) || !input.canHold(character)) {
          return Movements.BLOCKED;
        }
        return input.getMovementPenalty();
      }
    };
  }

  private ImmutableSet<Coordinate> getBlockedCoordinates() {
    return characters.fluent().transform(
        new Function<Character, Coordinate>() {
          @Override
          public Coordinate apply(Character input) {
            return input.getCoordinate();
          }
        }).toSet();
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