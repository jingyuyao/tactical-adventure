package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.HasCoordinate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Graph is an acyclic graph of nodes.
 */
public class Graph<T extends HasCoordinate> extends Node<T> {
    private final Set<Graph<T>> neighbors;

    Graph(T mapObject, Graph<T> parent, int distance) {
        super(mapObject, parent, distance);
        neighbors = new HashSet<Graph<T>>();
    }

    @Override
    protected void removeChild(Node<T> child) {
        getNeighbors().remove(child);
    }

    /**
     * Attempts to find a path to the given coordinates from this path.
     * @param x The target x coordinate
     * @param y The target y coordinate
     * @return A new single path to the coordinates, null if not possible
     */
    public Path<T> getPathTo(int x, int y) {
        // Parent should be set by the parent
        Path<T> path = new Path<T>(getMapObject(), null, getDistance());

        // Very basic depth first search
        if (x == getMapObject().getX() && y == getMapObject().getY()) {
            return path;
        }
        for (Graph<T> graph : getNeighbors()) {
            Path<T> found = graph.getPathTo(x, y);
            if (found != null) {
                found.changeParent(path);
                path.setChild(found);
                return path;
            }
        }

        return null;
    }

    /**
     * @return All objects contained in this graph
     */
    public Collection<T> getAllObjects() {
        Set<T> objects = new HashSet<T>();
        getAllObjects(objects);
        return objects;
    }

    void addNextPath(Graph<T> graph) {
        getNeighbors().add(graph);
    }

    private void getAllObjects(Set<T> accumulator) {
        accumulator.add(getMapObject());
        for (Graph<T> graph : getNeighbors()) {
            if (!accumulator.contains(graph.getMapObject())) {
                graph.getAllObjects(accumulator);
            }
        }
    }

    private Set<Graph<T>> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "neighbors.size()=" + getNeighbors().size() +
                "} " + super.toString();
    }
}
