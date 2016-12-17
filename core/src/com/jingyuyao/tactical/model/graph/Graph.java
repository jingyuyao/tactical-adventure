package com.jingyuyao.tactical.model.graph;

import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Graph is an acyclic graph of terrain nodes.
 */
public class Graph<T extends MapObject> extends Node<T> {
    private final Set<Graph<T>> neighbors;

    public Graph(T mapObject, Graph<T> parent, int distance) {
        super(mapObject, parent, distance);
        neighbors = new HashSet<Graph<T>>();
    }

    public void addNextPath(Graph<T> graph) {
        neighbors.add(graph);
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
        for (Graph<T> graph : neighbors) {
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
     * @return All {@link Terrain} contained in this path
     */
    public Collection<T> getAllObjects() {
        Set<T> terrains = new HashSet<T>();
        getAllObjects(terrains);
        return terrains;
    }

    private void getAllObjects(Set<T> accumulator) {
        accumulator.add(getMapObject());
        for (Graph<T> graph : neighbors) {
            if (!accumulator.contains(graph.getMapObject())) {
                graph.getAllObjects(accumulator);
            }
        }
    }

    @Override
    protected void removeChild(Node<T> child) {
        neighbors.remove(child);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "terrain=" + getMapObject() +
                ", neighbors=" + neighbors +
                ", parent.terrain=" + (getParent() != null ? getParent().getMapObject() : null) +
                ", distance=" + getDistance() +
                '}';
    }
}
