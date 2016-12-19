package com.jingyuyao.tactical.model.graph;

import com.google.common.graph.*;
import com.google.common.graph.Graph;
import com.jingyuyao.tactical.model.Grid;
import com.jingyuyao.tactical.model.HasCoordinate;

import java.util.*;

public class GraphAlgorithms {
    public static final int NO_EDGE = -1;

    /**
     * Find all nodes from ({@code startX}, {@code startY}) with a total path cost (sum of all the edge weights)
     * less or equal to {@code maxPathCost}. Resulting graph will contain data from {@code dataGrid}.
     * Inbound edge costs are assume to be equal for all nodes. Inbound edge costs comes from {@code edgeCostGrid}.
     *
     * @param dataGrid The grid to get data objects to put in the resulting graph
     * @param edgeCostGrid The grid to get edge cost for creating the graph
     * @param maxPathCost Maximum cost for the path between initial location to any other object
     */
    public static <O extends HasCoordinate> Graph<ValueNode<O>> createPathGraph(
            Grid<O> dataGrid,
            Grid<Integer> edgeCostGrid,
            int startX,
            int startY,
            int maxPathCost
    ) {
        MutableGraph<ValueNode<O>> graph =
                GraphBuilder
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
            processedObjects.add(minNode.getObject());

            for (O neighbor : dataGrid.getNeighbors(minNode.getObject().getX(), minNode.getObject().getY())) {
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
                    graph.putEdge(minNode, neighborNode);
                    pathCostMap.put(neighbor, pathCost);
                } else if (pathCost < pathCostMap.get(neighbor)) {
                    // Re-insert neighbor into the graph with (minNode, neighbor) as the only edge
                    graph.removeNode(neighborNode);
                    graph.putEdge(minNode, neighborNode);
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
    public static <O extends HasCoordinate> Collection<ValueNode<O>> findPathTo(
            Graph<ValueNode<O>> graph,
            int targetX,
            int targetY
    ) {
        if (graph.nodes().isEmpty()) {
            return Collections.emptyList();
        }

        LinkedList<ValueNode<O>> accumulator = new LinkedList<ValueNode<O>>();
        Set<ValueNode<O>> visited = new HashSet<ValueNode<O>>();
        ValueNode<O> firstNode = graph.nodes().iterator().next();
        if (findPathTo(graph, targetX, targetY, firstNode, accumulator, visited)) {
            return accumulator;
        }

        return Collections.emptyList();
    }

    private static <O extends HasCoordinate> boolean findPathTo(
            Graph<ValueNode<O>> graph,
            int targetX,
            int targetY,
            ValueNode<O> currentNode,
            LinkedList<ValueNode<O>> accumulator,
            Set<ValueNode<O>> visited
    ) {
        if (visited.contains(currentNode)) {
            return false;
        }
        visited.add(currentNode);

        if (targetX == currentNode.getObject().getX() && targetY == currentNode.getObject().getY()) {
            accumulator.addLast(currentNode);
            return true;
        }

        for (ValueNode<O> neighbor : graph.adjacentNodes(currentNode)) {
            if (findPathTo(graph, targetX, targetY, neighbor, accumulator, visited)) {
                accumulator.addFirst(neighbor);
                return true;
            }
        }

        return false;
    }
}
