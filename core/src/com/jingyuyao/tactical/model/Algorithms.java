package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.*;

import java.util.*;
import java.util.Map;

/**
 * Functions should return immutable objects.
 */
class Algorithms {
    static final int NO_EDGE = -1;

    /**
     * Creates a directed, acyclic graph starting at {@code (startX, startY)} in {@code coordinateGrid} that contains
     * all reachable nodes whose total path cost (sum of all edge weights from the start to the current node)
     * is less or equal to {@code maxPathCost}. Inbound edge costs comes from {@code edgeCostGrid}.
     *
     * @param edgeCostGrid The grid to get edge cost for creating the graph
     * @param maxPathCost Maximum cost for the path between initial location to any other object
     */
    static ValueGraph<Coordinate, Integer> minPathSearch(
            Grid<Integer> edgeCostGrid,
            Coordinate startingCoordinate,
            int maxPathCost
    ) {
        MutableValueGraph<Coordinate, Integer> graph =
                ValueGraphBuilder
                        .directed()
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

            for (Coordinate neighbor : edgeCostGrid.getNeighbors(minCoordinate)) {
                if (processedCoordinates.contains(neighbor)) {
                    continue;
                }

                int edgeCost = edgeCostGrid.get(neighbor);
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

        Preconditions.checkState(!Graphs.hasCycle(graph), "Cycle in minPathSearch");
        return graph;
    }

    /**
     * Attempts to find a path to {@code target}.
     *
     * @param graph An directed acyclic graph
     * @param target The target node to find a path to
     * @return A path to {@code target} from the first node in the graph or an empty collection if target is
     * not in the graph
     */
    static ImmutableList<Coordinate> findPathTo(Graph<Coordinate> graph, Coordinate target) {
        if (!graph.nodes().contains(target)) {
            return ImmutableList.of();
        }

        ImmutableList.Builder<Coordinate> builder = new ImmutableList.Builder<Coordinate>();
        builder.add(target);

        Set<Coordinate> predecessors = graph.predecessors(target);
        while (predecessors.size() != 0) {
            Preconditions.checkState(
                    predecessors.size() == 1,
                    "findPathTo encountered a node with multiple predecessors"
            );
            Coordinate predecessor = predecessors.iterator().next();
            builder.add(predecessor);
            predecessors = graph.predecessors(predecessor);
        }

        return builder.build().reverse();
    }

    /**
     * Find all the objects on the grid that is {@code distance} away from the starting point.
     *
     * @param grid The grid the coordinate is contained in, used to find neighbors
     */
    static ImmutableList<Coordinate> findNDistanceAway(
            Grid<?> grid,
            Coordinate startingCoordinate,
            int distance
    ) {
        ImmutableList.Builder<Coordinate> builder = new ImmutableList.Builder<Coordinate>();
        findNDistanceAway(grid, startingCoordinate, distance, builder);
        return builder.build();
    }

    private static void findNDistanceAway(
        Grid<?> grid,
        Coordinate currentCoordinate,
        int distanceRemaining,
        ImmutableList.Builder<Coordinate> builder
    ) {
        if (distanceRemaining == 0) {
            builder.add(currentCoordinate);
            return;
        }

        for (Coordinate neighbor : grid.getNeighbors(currentCoordinate)) {
            findNDistanceAway(grid, neighbor, distanceRemaining-1, builder);
        }
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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValueNode<?> valueNode = (ValueNode<?>) o;
            return com.google.common.base.Objects.equal(object, valueNode.object);
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(object);
        }
    }
}
