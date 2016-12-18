package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;
import com.jingyuyao.tactical.model.HasGrid;

import java.util.*;

public class GraphFactory {
    /**
     * Creates a graph that is reachable from {@code (startX, startY)} on {@code grid} with a max
     * distance of {@code maxDistanceCost}. Distance cost of each location in the grid is calculated
     * in relation to {@code data}.
     *
     * @param grid The grid this graph is created on
     * @param startX Initial x coordinate on the grid
     * @param startY Initial y coordinate on the grid
     * @param maxDistanceCost Maximum cost for the path between initial location to any reachable node
     * @param data The data used to do distance cost comparison with
     */
    public static <D, T extends HasCoordinate & HasDistanceCost<D>> Graph<T> createReachableGraph(
            HasGrid<T> grid, int startX, int startY, int maxDistanceCost, D data) {
        T startingObj = grid.get(startX, startY);
        java.util.Map<T, Graph<T>> graphMap = new HashMap<T, Graph<T>>();
        PriorityQueue<Graph<T>> minNodeQueue = new PriorityQueue<Graph<T>>();
        Graph<T> startingNode = new Graph<T>(startingObj, null, 0);
        Set<T> visited = new HashSet<T>();

        minNodeQueue.add(startingNode);
        while (!minNodeQueue.isEmpty()) {
            Graph<T> minGraph = minNodeQueue.poll();
            visited.add(minGraph.getMapObject());

            for (T neighbor : GraphFactory.getAdjacent(grid, minGraph.getMapObject())) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int distanceCost = neighbor.getDistanceCost(data);
                if (distanceCost == HasDistanceCost.CANT_CROSS) {
                    continue;
                }

                int newDistance = minGraph.getDistance() + distanceCost;
                if (newDistance > maxDistanceCost) {
                    continue;
                }

                Graph<T> neighborGraph;
                if (graphMap.containsKey(neighbor)) {
                    neighborGraph = graphMap.get(neighbor);
                    minNodeQueue.remove(neighborGraph);
                    if (newDistance < neighborGraph.getDistance()) {
                        neighborGraph.changeParent(minGraph);
                        neighborGraph.setDistance(newDistance);
                        minGraph.addNextPath(neighborGraph);
                    }
                } else {
                    neighborGraph = new Graph<T>(neighbor, minGraph, newDistance);
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
