package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.HasEdgeCost;

public class Terrain extends MapObject implements HasEdgeCost<Character> {
    private Type type;
    private PotentialTarget potentialTarget = PotentialTarget.NONE;

    Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    @Override
    public int getEdgeCost(Character character) {
        if (!character.getCanCrossTerrainTypes().contains(type)) {
            return HasEdgeCost.NO_EDGE;
        }

        switch (type) {
            case OBSTRUCTED:
                return 2;
            case WATER:
            case MOUNTAIN:
                return 3;
            case NORMAL:
            default:
                return 1;
        }
    }

    public PotentialTarget getPotentialTarget() {
        return potentialTarget;
    }

    void setPotentialTarget(PotentialTarget potentialTarget) {
        this.potentialTarget = potentialTarget;
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }

    public enum PotentialTarget {
        NONE,
        REACHABLE,
        CAN_ATTACK
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "type=" + type +
                ", potentialTarget=" + potentialTarget +
                "} " + super.toString();
    }
}
