package com.jingyuyao.tactical.model.graph;

import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.HasCoordinate;

import java.util.*;

public class Graph<T extends HasCoordinate> extends Node<T> {
    private final Set<Graph<T>> neighbors;

    Graph(T mapObject, Graph<T> parent, int pathCost) {
        super(mapObject, parent, pathCost);
        neighbors = new HashSet<Graph<T>>();
    }

    @Override
    protected void removeChild(Node<T> child) {
        neighbors.remove(child);
    }

    /**
     * Attempts to find a path to the given coordinates from this path.
     * @param x The target x coordinate
     * @param y The target y coordinate
     * @return A new single path to the coordinates, null if not possible
     */
    public Optional<Path<T>> getPathTo(int x, int y) {
        // Parent should be set by the parent
        Path<T> path = new Path<T>(getObject(), null, getPathCost());

        // Very basic depth first search
        if (x == getObject().getX() && y == getObject().getY()) {
            return Optional.of(path);
        }
        for (Graph<T> neighbor : neighbors) {
            Optional<Path<T>> neighborPath = neighbor.getPathTo(x, y);

            if (neighborPath.isPresent()) {
                neighborPath.get().changeParent(path);
                path.setChild(neighborPath.get());
                return Optional.of(path);
            }
        }

        return Optional.absent();
    }

    /**
     * @return All objects contained in this graph
     */
    public Collection<T> getAllObjects() {
        Set<T> objects = new HashSet<T>();
        getAllObjects(objects);
        return objects;
    }

    void addNeighbor(Graph<T> neighbor) {
        neighbors.add(neighbor);
    }

    private void getAllObjects(Set<T> accumulator) {
        accumulator.add(getObject());
        for (Graph<T> graph : neighbors) {
            if (!accumulator.contains(graph.getObject())) {
                graph.getAllObjects(accumulator);
            }
        }
    }

    @Override
    public String toString() {
        return "Graph{" +
                "neighbors.size()=" + neighbors.size() +
                "} " + super.toString();
    }
}
