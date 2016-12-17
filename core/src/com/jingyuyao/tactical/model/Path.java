package com.jingyuyao.tactical.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Path is an acyclic graph of terrain nodes.
 */
public class Path implements Comparable<Path> {
    private final Terrain terrain;
    private final Set<Path> nextPaths;
    private Path parent;
    private int distance;

    Path(Terrain terrain, Path parent, int distance) {
        this.terrain = terrain;
        this.parent = parent;
        this.distance = distance;
        nextPaths = new HashSet<Path>();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * @return The list of terrain starting from this path to to the end of the path
     */
    public Collection<Terrain> getTerrainRoute() {
        Collection<Terrain> paths = new ArrayList<Terrain>();
        getTerrainRoute(paths);
        return paths;
    }

    void changeParent(Path newParent, int newDistance) {
        parent.nextPaths.remove(this);
        parent = newParent;
        distance = newDistance;
    }

    void addNextPath(Path path) {
        nextPaths.add(path);
    }

    int getDistance() {
        return distance;
    }

    /**
     * Attempts to find a path to the given coordinates from this path.
     * @param x The target x coordinate
     * @param y The target y coordinate
     * @return A new single path to the coordinates, null if not possible
     */
    Path getPathTo(int x, int y) {
        // Parent should be set by the parent
        Path newThis = new Path(terrain, null, distance);

        // Very basic depth first search
        if (x == terrain.getX() && y == terrain.getY()) {
            return newThis;
        }
        for (Path path : nextPaths) {
            Path found = path.getPathTo(x, y);
            if (found != null) {
                found.parent = newThis;
                newThis.addNextPath(found);
                return newThis;
            }
        }

        return null;
    }

    /**
     * @return All {@link Terrain} contained in this path
     */
    Collection<Terrain> getAllTerrains() {
        Set<Terrain> terrains = new HashSet<Terrain>();
        getAllTerrains(terrains);
        return terrains;
    }

    private void getTerrainRoute(Collection<Terrain> accumulator) {
        accumulator.add(terrain);
        if (!nextPaths.isEmpty()) {
            Path first = nextPaths.iterator().next();
            first.getTerrainRoute(accumulator);
        }
    }

    private void getAllTerrains(Set<Terrain> accumulator) {
        accumulator.add(terrain);
        for (Path path : nextPaths) {
            if (!accumulator.contains(path.getTerrain())) {
                path.getAllTerrains(accumulator);
            }
        }
    }

    @Override
    public int compareTo(Path path) {
        return distance - path.distance;
    }

    @Override
    public String toString() {
        return "Path{" +
                "terrain=" + terrain +
                ", nextPaths=" + nextPaths +
                ", parent.terrain=" + (parent != null ? parent.terrain : null) +
                ", distance=" + distance +
                '}';
    }
}
