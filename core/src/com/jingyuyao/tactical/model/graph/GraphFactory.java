package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;
import com.jingyuyao.tactical.model.HasGrid;

import java.util.*;

public class GraphFactory {
    /**
     * Starting at {@code grid.get(startX, startY)}
     * Dijkstra's path finding up to {@code maxDistance} weight
     */
    public static <T extends HasCoordinate & HasWeight> Graph<T> createReachableGraph(
            HasGrid<T> grid, int startX, int startY, int maxDistance) {
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

                int distance = minGraph.getDistance() + neighbor.getWeight();
                if (distance > maxDistance) {
                    continue;
                }

                Graph<T> neighborGraph;
                if (graphMap.containsKey(neighbor)) {
                    neighborGraph = graphMap.get(neighbor);
                    minNodeQueue.remove(neighborGraph);
                    if (distance < neighborGraph.getDistance()) {
                        neighborGraph.changeParent(minGraph);
                        neighborGraph.setDistance(distance);
                        minGraph.addNextPath(neighborGraph);
                    }
                } else {
                    neighborGraph = new Graph<T>(neighbor, minGraph, distance);
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
