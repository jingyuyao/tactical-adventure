package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.jingyuyao.tactical.model.*;

import java.util.Observable;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState extends Observable {
    private AbstractState state;

    public MapState(Map map, Turn turn) {
        state = new Waiting(this, map, turn, new Markings(map));
    }

    public ImmutableCollection<Action> getActions() {
        return state.getActions();
    }

    public void select(Player player) {
        state.select(player);
    }

    public void select(Enemy enemy) {
        state.select(enemy);
    }

    public void select(Terrain terrain) {
        state.select(terrain);
    }

    // Testing
    public String getStateName() {
        return state.getClass().getSimpleName();
    }

    /**
     * Changes state to {@code newState} if it is not null.
     */
    void changeStateTo(AbstractState newState) {
        if (newState != null) {
            state.exit();
            newState.enter();
            state = newState;

            setChanged();
            notifyObservers();
        }
    }
}
