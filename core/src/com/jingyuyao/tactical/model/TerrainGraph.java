package com.jingyuyao.tactical.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * TerrainGraph is an acyclic graph of terrain nodes.
 */
public class TerrainGraph extends TerrainNode {
    private final Set<TerrainGraph> neighbors;

    TerrainGraph(Terrain terrain, TerrainGraph parent, int distance) {
        super(terrain, parent, distance);
        neighbors = new HashSet<TerrainGraph>();
    }

    void addNextPath(TerrainGraph terrainGraph) {
        neighbors.add(terrainGraph);
    }

    /**
     * Attempts to find a path to the given coordinates from this path.
     * @param x The target x coordinate
     * @param y The target y coordinate
     * @return A new single path to the coordinates, null if not possible
     */
    TerrainPath getPathTo(int x, int y) {
        // Parent should be set by the parent
        TerrainPath path = new TerrainPath(getTerrain(), null, getDistance());

        // Very basic depth first search
        if (x == getTerrain().getX() && y == getTerrain().getY()) {
            return path;
        }
        for (TerrainGraph terrainGraph : neighbors) {
            TerrainPath found = terrainGraph.getPathTo(x, y);
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
    Collection<Terrain> getAllTerrains() {
        Set<Terrain> terrains = new HashSet<Terrain>();
        getAllTerrains(terrains);
        return terrains;
    }

    private void getAllTerrains(Set<Terrain> accumulator) {
        accumulator.add(getTerrain());
        for (TerrainGraph terrainGraph : neighbors) {
            if (!accumulator.contains(terrainGraph.getTerrain())) {
                terrainGraph.getAllTerrains(accumulator);
            }
        }
    }

    @Override
    protected void removeChild(TerrainNode child) {
        neighbors.remove(child);
    }

    @Override
    public String toString() {
        return "TerrainGraph{" +
                "terrain=" + getTerrain() +
                ", neighbors=" + neighbors +
                ", parent.terrain=" + (getParent() != null ? getParent().getTerrain() : null) +
                ", distance=" + getDistance() +
                '}';
    }
}
