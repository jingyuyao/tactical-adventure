package com.jingyuyao.tactical.model.object;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.map.Terrain;

import java.util.Set;

/**
 * Setters should be package private.
 */
public class Stats {
    // TODO: remove me
    private static final int MAX_HP = 20;

    private final Set<Terrain.Type> passableTerrainTypes;
    /**
     * >= 0
     */
    private int hp;
    private int moveDistance;

    public Stats(int hp, int moveDistance, Set<Terrain.Type> passableTerrainTypes) {
        Preconditions.checkArgument(hp > 0);
        this.hp = hp;
        this.passableTerrainTypes = passableTerrainTypes;
        this.moveDistance = moveDistance;
    }

    int getHp() {
        return hp;
    }

    int getMoveDistance() {
        return moveDistance;
    }

    /**
     * Reduce {@link #hp} by {@code delta}.
     * @return whether {@link #hp} <= 0
     */
    boolean damageBy(int delta) {
        hp = Math.max(hp - delta, 0);
        return hp == 0;
    }

    void healBy(int delta) {
        hp = Math.min(hp + delta, MAX_HP);
    }

    boolean canPassTerrainType(Terrain.Type terrainType) {
        return passableTerrainTypes.contains(terrainType);
    }

    @Override
    public String toString() {
        return "Stats{" +
                "hp=" + hp +
                ", moveDistance=" + moveDistance +
                '}';
    }
}
