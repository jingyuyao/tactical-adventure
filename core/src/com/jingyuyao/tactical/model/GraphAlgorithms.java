package com.jingyuyao.tactical.model;

import com.google.common.graph.*;

import java.util.*;
import java.util.Map;

class GraphAlgorithms {
    static final int NO_EDGE = -1;

    /**
     * Find all nodes from ({@code startX}, {@code startY}) with a total path cost (sum of all the edge weights)
     * less or equal to {@code maxPathCost}. Resulting graph will contain data from {@code dataGrid}.
     * Inbound edge costs are assume to be equal for all nodes. Inbound edge costs comes from {@code edgeCostGrid}.
     *
     * @param dataGrid The grid to get data objects to put in the resulting graph
     * @param edgeCostGrid The grid to get edge cost for creating the graph
     * @param maxPathCost Maximum cost for the path between initial location to any other object
     */
    static <O extends HasCoordinate> ValueGraph<O, Integer> createPathGraph(
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

        if (Graphs.hasCycle(graph)) {
            throw new RuntimeException("Cycle in createPathGraph");
        }

        return graph;
    }

    /**
     * Attempts to find a path to the given coordinates from this path.
     * @param targetX The target x coordinate
     * @param targetY The target y coordinate
     * @return A path to the target coordinates, empty if no path
     */
    static <O extends HasCoordinate> Collection<O> findPathTo(
            Graph<O> graph,
            int targetX,
            int targetY
    ) {
        if (graph.nodes().isEmpty()) {
            return Collections.emptyList();
        }

        LinkedList<O> accumulator = new LinkedList<O>();
        Set<O> visited = new HashSet<O>();
        O firstNode = graph.nodes().iterator().next();
        if (findPathTo(graph, targetX, targetY, firstNode, accumulator, visited)) {
            return accumulator;
        }

        return Collections.emptyList();
    }

    private static <O extends HasCoordinate> boolean findPathTo(
            Graph<O> graph,
            int targetX,
            int targetY,
            O currentNode,
            LinkedList<O> accumulator,
            Set<O> visited
    ) {
        if (visited.contains(currentNode)) {
            return false;
        }
        visited.add(currentNode);

        if (targetX == currentNode.getX() && targetY == currentNode.getY()) {
            accumulator.addLast(currentNode);
            return true;
        }

        for (O neighbor : graph.adjacentNodes(currentNode)) {
            if (findPathTo(graph, targetX, targetY, neighbor, accumulator, visited)) {
                accumulator.addFirst(neighbor);
                return true;
            }
        }

        return false;
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
