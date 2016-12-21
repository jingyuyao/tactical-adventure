package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.state.SelectionState;
import com.jingyuyao.tactical.model.state.Waiting;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class Selector {
    private SelectionState selectionState;

    Selector(Map map, Selections selections) {
        selectionState = new Waiting(map, selections);
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
