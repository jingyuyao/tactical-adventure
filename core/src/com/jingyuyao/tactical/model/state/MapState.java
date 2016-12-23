package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.*;

import java.util.Observable;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState extends Observable {
    private AbstractState state;
    private boolean animating = false;

    public MapState(Map map, Turn turn) {
        state = new Waiting(this, map, turn, new Markings(map));
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
        setChanged();
        notifyObservers();
    }

    public ImmutableCollection<Action> getActions() {
        // Don't expose actions since they can change state
        if (animating) {
            return ImmutableList.of();
        }
        return state.getActions();
    }

    public void select(Player player) {
        if (animating) return;
        state.select(player);
    }

    public void select(Enemy enemy) {
        if (animating) return;
        state.select(enemy);
    }

    public void select(Terrain terrain) {
        if (animating) return;
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
