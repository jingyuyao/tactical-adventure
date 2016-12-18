package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.HasWeight;

public class Terrain extends MapObject implements HasWeight {
    private Type type;
    private PotentialTarget potentialTarget = PotentialTarget.NONE;

    Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    @Override
    public int getWeight() {
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
        ATTACKABLE
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "type=" + type +
                ", potentialTarget=" + potentialTarget +
                "} " + super.toString();
    }
}
