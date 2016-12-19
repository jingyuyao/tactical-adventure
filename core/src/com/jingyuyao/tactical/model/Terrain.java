package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.graph.GraphAlgorithms;

public class Terrain extends MapObject {
    private Type type;
    private SelectionMode selectionMode = SelectionMode.NONE;

    public Terrain(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    int getMovementPenality(Character character) {
        if (!character.getCanCrossTerrainTypes().contains(type)) {
            return GraphAlgorithms.NO_EDGE;
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
