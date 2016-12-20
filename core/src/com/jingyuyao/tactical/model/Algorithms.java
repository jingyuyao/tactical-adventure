package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.google.common.graph.*;

import java.util.*;
import java.util.Map;

class Algorithms {
    static final int NO_EDGE = -1;

    /**
     * Creates a directed, acyclic graph starting at {@code (startX, startY)} in {@code dataGrid} that contains
     * all reachable nodes whose total path cost (sum of all edge weights from the start to the current node)
     * is less or equal to {@code maxPathCost}. Inbound edge costs comes from {@code edgeCostGrid}.
     *
     * @param dataGrid The grid to get data objects to put in the resulting graph
     * @param edgeCostGrid The grid to get edge cost for creating the graph
     * @param maxPathCost Maximum cost for the path between initial location to any other object
     */
    static <O extends HasCoordinate>
    ValueGraph<O, Integer> minPathSearch(
            Grid<O> dataGrid,
            Grid<Integer> edgeCostGrid,
            int startX,
            int startY,
            int maxPathCost
    ) {
        MutableValueGraph<O, Integer> graph =
                ValueGraphBuilder
                        .directed()
                        .allowsSelfLoops(false)
                        .nodeOrder(ElementOrder.insertion())
                        .build();
        Set<O> processedObjects = new HashSet<O>();
        Map<O, Integer> pathCostMap = new HashMap<O, Integer>();
        Queue<ValueNode<O>> minNodeQueue = new PriorityQueue<ValueNode<O>>();

        O startingObject = dataGrid.get(startX, startY);
        pathCostMap.put(startingObject, 0);
        minNodeQueue.add(new ValueNode<O>(startingObject, 0));

        // Dijkstra's algorithm
        while (!minNodeQueue.isEmpty()) {
            ValueNode<O> minNode = minNodeQueue.poll();
            O minObject = minNode.getObject();
            processedObjects.add(minObject);

            for (O neighbor : dataGrid.getNeighbors(minObject.getX(), minObject.getY())) {
                if (processedObjects.contains(neighbor)) {
                    continue;
                }

                int edgeCost = edgeCostGrid.get(neighbor.getX(), neighbor.getY());
                if (edgeCost == NO_EDGE) {
                    continue;
                }

                int pathCost = minNode.getValue() + edgeCost;
                if (pathCost > maxPathCost) {
                    continue;
                }

                ValueNode<O> neighborNode = new ValueNode<O>(neighbor, pathCost);
                if (!pathCostMap.containsKey(neighbor)) {
                    graph.putEdgeValue(minObject, neighbor, pathCost);
                    pathCostMap.put(neighbor, pathCost);
                } else if (pathCost < pathCostMap.get(neighbor)) {
                    // Remove neighbor from graph so that (minNode, neighbor) is the only edge
                    graph.removeNode(neighbor);
                    graph.putEdgeValue(minObject, neighbor, pathCost);
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
    static <O> Collection<O> findPathTo(Graph<O> graph, O target) {
        if (!graph.nodes().contains(target)) {
            return Collections.emptyList();
        }

        List<O> path = new ArrayList<O>();
        path.add(target);

        Set<O> predecessors = graph.predecessors(target);
        while (predecessors.size() != 0) {
            Preconditions.checkState(
                    predecessors.size() == 1,
                    "findPathTo encountered a node with multiple predecessors"
            );
            O predecessor = predecessors.iterator().next();
            path.add(predecessor);
            predecessors = graph.predecessors(predecessor);
        }

        Collections.reverse(path);

        return path;
    }

    /**
     * Find all the objects on the grid that is {@code distance} away from the starting point.
     */
    static <O extends HasCoordinate> Collection<O> findNDistanceAway(
            Grid<O> grid,
            int startX,
            int startY,
            int distance
    ) {
        Collection<O> nDistanceAway = new ArrayList<O>();
        findNDistanceAway(grid, startX, startY, distance, nDistanceAway);
        return  nDistanceAway;
    }

    private static <O extends HasCoordinate> void findNDistanceAway(
        Grid<O> grid,
        int currentX,
        int currentY,
        int distanceRemaining,
        Collection<O> accumulator
    ) {
        O current = grid.get(currentX, currentY);

        if (distanceRemaining == 0) {
            accumulator.add(current);
            return;
        }

        for (O neighbor : grid.getNeighbors(currentX, currentY)) {
            findNDistanceAway(
                    grid, neighbor.getX(), neighbor.getY(), distanceRemaining-1, accumulator);
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
