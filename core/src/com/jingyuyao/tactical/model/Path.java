package com.jingyuyao.tactical.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Path is an acyclic graph of terrain nodes
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

    public void updateParent(Path newParent, int newDistance) {
        parent.nextPaths.remove(this);
        parent = newParent;
        distance = newDistance;
    }

    public void addNextPath(Path path) {
        nextPaths.add(path);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int getDistance() {
        return distance;
    }

    /**
     * @return All {@link Terrain} reachable starting at this path
     */
    public Collection<Terrain> getReachableTerrains() {
        Set<Terrain> terrains = new HashSet<Terrain>();
        getReachableTerrains(terrains);
        return terrains;
    }

    private void getReachableTerrains(Set<Terrain> accumulator) {
        accumulator.add(terrain);
        for (Path path : nextPaths) {
            if (!accumulator.contains(path.getTerrain())) {
                path.getReachableTerrains(accumulator);
            }
        }
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

    @Override
    public int compareTo(Path path) {
        return distance - path.distance;
    }
}
