package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.HasDistanceCost;

public class Terrain extends MapObject implements HasDistanceCost<Character> {
    private Type type;
    private PotentialTarget potentialTarget = PotentialTarget.NONE;

    Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    @Override
    public int getDistanceCost(Character character) {
        if (!character.getCanCrossTerrainTypes().contains(type)) {
            return HasDistanceCost.CANT_CROSS;
        }

        switch (type) {
            case OBSTRUCTED:
                return 2;
            case WATER:
            case MOUNTAIN:
                // TODO: Figure out how "blocking" terrains work
                return 1000;
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
