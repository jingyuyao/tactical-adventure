package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Algorithms;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.state.MapState;

public class Terrain extends AbstractObject {
    private Type type;

    public Terrain(EventBus eventBus, int x, int y, Type type) {
        super(eventBus, x, y);
        this.type = type;
    }

    @Override
    public void select(MapState mapState) {
        mapState.select(this);
    }

    @Override
    public void highlight(Highlighter highlighter) {
        highlighter.highlight(this);
    }

    public Type getType() {
        return type;
    }

    public int getMovementPenalty(Character character) {
        if (!character.canPassTerrainType(type)) {
            return Algorithms.NO_EDGE;
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

    @Override
    public String toString() {
        return "Terrain{" +
                ", type=" + type +
                "} " + super.toString();
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }
}
