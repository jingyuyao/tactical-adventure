package com.jingyuyao.tactical.model.object;

import java.util.Set;

public class Stats {
    private final Set<Terrain.Type> passableTerrainTypes;
    private int moveDistance;

    public Stats(Set<Terrain.Type> passableTerrainTypes, int moveDistance) {
        this.passableTerrainTypes = passableTerrainTypes;
        this.moveDistance = moveDistance;
    }

    public int getMoveDistance() {
        return moveDistance;
    }

    boolean canPassTerrainType(Terrain.Type terrainType) {
        return passableTerrainTypes.contains(terrainType);
    }

    @Override
    public String toString() {
        return "Stats{" +
                "moveDistance=" + moveDistance +
                '}';
    }
}
