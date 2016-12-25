package com.jingyuyao.tactical.model.object;

import com.google.common.base.Preconditions;

import java.util.Set;

/**
 * Setters should be package private.
 */
public class Stats {
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

    public int getHp() {
        return hp;
    }

    public int getMoveDistance() {
        return moveDistance;
    }

    /**
     * Reduce {@link #hp} by {@code delta}.
     * @return whether {@link #hp} <= 0
     */
    boolean damageBy(int delta) {
        hp -= delta;
        if (hp < 0) {
            hp = 0;
        }
        return hp == 0;
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
