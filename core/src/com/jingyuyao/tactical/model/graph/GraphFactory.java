package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;
import com.jingyuyao.tactical.model.HasGrid;

import java.util.*;

public class GraphFactory {
    /**
     * Creates a graph that is reachable from {@code (startX, startY)} on {@code grid} with a max
     * edge cost of of {@code maxCumulativeEdgeCost}. Edge cost for each location in the grid is calculated
     * in relation to {@code data}.
     *
     * @param grid The grid this graph is created on
     * @param startX Initial x coordinate on the grid
     * @param startY Initial y coordinate on the grid
     * @param maxCumulativeEdgeCost Maximum cost for the path between initial location to any reachable node
     * @param data The data used to calculate edge cost
     */
    // TODO: Need a way to avoid other characters
    public static <D, T extends HasCoordinate & HasEdgeCost<D>> Graph<T> createReachableGraph(
            HasGrid<T> grid, int startX, int startY, int maxCumulativeEdgeCost, D data) {
        T startingObj = grid.get(startX, startY);
        java.util.Map<T, Graph<T>> graphMap = new HashMap<T, Graph<T>>();
        PriorityQueue<Graph<T>> minNodeQueue = new PriorityQueue<Graph<T>>();
        Graph<T> startingNode = new Graph<T>(startingObj, null, 0);
        Set<T> visited = new HashSet<T>();

        minNodeQueue.add(startingNode);
        while (!minNodeQueue.isEmpty()) {
            Graph<T> minGraph = minNodeQueue.poll();
            visited.add(minGraph.getObject());

            for (T neighbor : GraphFactory.getAdjacent(grid, minGraph.getObject())) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int edgeCost = neighbor.getEdgeCost(data);
                if (edgeCost == HasEdgeCost.NO_EDGE) {
                    continue;
                }

                int cumulativeEdgeCost = minGraph.getCumulativeEdgeCost() + edgeCost;
                if (cumulativeEdgeCost > maxCumulativeEdgeCost) {
                    continue;
                }

                Graph<T> neighborGraph;
                if (graphMap.containsKey(neighbor)) {
                    neighborGraph = graphMap.get(neighbor);
                    minNodeQueue.remove(neighborGraph);
                    if (cumulativeEdgeCost < neighborGraph.getCumulativeEdgeCost()) {
                        neighborGraph.changeParent(minGraph);
                        neighborGraph.setCumulativeEdgeCost(cumulativeEdgeCost);
                        minGraph.addNextPath(neighborGraph);
                    }
                } else {
                    neighborGraph = new Graph<T>(neighbor, minGraph, cumulativeEdgeCost);
                    minGraph.addNextPath(neighborGraph);
                    graphMap.put(neighbor, neighborGraph);
                }
                minNodeQueue.add(neighborGraph);
            }
        }

        return startingNode;
    }

    private static <T extends HasCoordinate> Collection<T> getAdjacent(HasGrid<T> grid, T obj) {
        int x = obj.getX();
        int y = obj.getY();
        Collection<T> neighbors = new ArrayList<T>(4);

        if (x > 0) {
            neighbors.add(grid.get(x - 1, y));
        }
        if (x < grid.getWidth() - 1) {
            neighbors.add(grid.get(x + 1, y));
        }
        if (y > 0) {
            neighbors.add(grid.get(x, y - 1));
        }
        if (y < grid.getHeight() - 1) {
            neighbors.add(grid.get(x, y + 1));
        }

        return neighbors;
    }
}
