package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.SelectionState;
import com.jingyuyao.tactical.model.state.Waiting;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class Selector {
    private SelectionState selectionState;

    public Selector(Map map) {
        selectionState = new Waiting(map, new Selections(map));
    }

    void select(Player player) {
        selectionState = selectionState.select(player);
    }

    void select(Enemy enemy) {
        selectionState = selectionState.select(enemy);
    }

    void select(Terrain terrain) {
        selectionState = selectionState.select(terrain);
    }
}
