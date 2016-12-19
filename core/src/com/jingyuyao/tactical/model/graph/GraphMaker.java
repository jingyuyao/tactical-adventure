package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.Grid;
import com.jingyuyao.tactical.model.HasCoordinate;

import java.util.*;

public class GraphMaker {
    public static final int NO_EDGE = -1;

    /**
     * Find all nodes from ({@code startX}, {@code startY}) with a total path cost (sum of all the edge weights)
     * less or equal to {@code maxPathCost}. Resulting graph will contain data from {@code dataGrid}.
     * Inbound edge costs are assume to be equal for all nodes. Inbound edge costs comes from {@code edgeCostGrid}.
     *
     * @param dataGrid The grid to get data objects to put in the resulting graph
     * @param edgeCostGrid The grid to get edge cost for creating the graph
     * @param startX Initial x coordinate on the edgeCostGrid
     * @param startY Initial y coordinate on the edgeCostGrid
     * @param maxPathCost Maximum cost for the path between initial location to any other node
     */
    public static <T extends HasCoordinate> Graph<T> createPathGraph(
            Grid<T> dataGrid,
            Grid<Integer> edgeCostGrid,
            int startX,
            int startY,
            int maxPathCost) {
        java.util.Map<T, Graph<T>> graphMap = new HashMap<T, Graph<T>>();
        Queue<Graph<T>> minGraphQueue = new PriorityQueue<Graph<T>>();
        Graph<T> startingGraph = new Graph<T>(dataGrid.get(startX, startY), null, 0);
        Set<T> visited = new HashSet<T>();
        minGraphQueue.add(startingGraph);

        // Dijkstra's algorithm
        while (!minGraphQueue.isEmpty()) {
            Graph<T> minGraph = minGraphQueue.poll();
            T obj = minGraph.getObject();
            visited.add(obj);

            for (T neighbor : dataGrid.getNeighbors(obj.getX(), obj.getY())) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int edgeCost = edgeCostGrid.get(neighbor.getX(), neighbor.getY());
                if (edgeCost == NO_EDGE) {
                    continue;
                }

                int cumulativeEdgeCost = minGraph.getPathCost() + edgeCost;
                if (cumulativeEdgeCost > maxPathCost) {
                    continue;
                }

                Graph<T> neighborGraph;
                if (graphMap.containsKey(neighbor)) {
                    neighborGraph = graphMap.get(neighbor);
                    minGraphQueue.remove(neighborGraph);
                    if (cumulativeEdgeCost < neighborGraph.getPathCost()) {
                        neighborGraph.changeParent(minGraph);
                        neighborGraph.setPathCost(cumulativeEdgeCost);
                        minGraph.addNeighbor(neighborGraph);
                    }
                } else {
                    neighborGraph = new Graph<T>(neighbor, minGraph, cumulativeEdgeCost);
                    minGraph.addNeighbor(neighborGraph);
                    graphMap.put(neighbor, neighborGraph);
                }
                minGraphQueue.add(neighborGraph);
            }
        }

        return startingGraph;
    }
}
