package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.MapState;

import java.util.Observable;

/**
 * An object on the game grid.
 */
public abstract class MapObject extends Coordinate {
    MapObject(int x, int y) {
        super(x, y);
    }

    /**
     * Enables the visitor pattern for selection.
     *
     * I can't believe OOD actually taught me something useful.
     */
    public abstract void select(MapState mapState);
}
