package com.jingyuyao.tactical.model;

public class Terrain extends MapObject {
    private Type type;
    private PotentialTarget potentialTarget = PotentialTarget.NONE;

    Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    public PotentialTarget getPotentialTarget() {
        return potentialTarget;
    }

    public void setPotentialTarget(PotentialTarget potentialTarget) {
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
