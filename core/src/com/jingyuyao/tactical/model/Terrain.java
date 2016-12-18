package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.HasEdgeCost;

public class Terrain extends MapObject implements HasEdgeCost<Character> {
    private Type type;
    private SelectionMode selectionMode = SelectionMode.NONE;

    public Terrain(int x, int y, Type type) {
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

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        update();
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }

    public enum SelectionMode {
        NONE,
        MOVE,
        ATTACK,
        DANGER,
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "type=" + type +
                ", selectionMode=" + selectionMode +
                "} " + super.toString();
    }
}
