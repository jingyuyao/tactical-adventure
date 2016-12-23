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
    private final AnimationCounter animationCounter;
    private AbstractState state;

    public MapState(Map map, Turn turn, AnimationCounter animationCounter) {
        this.animationCounter = animationCounter;
        state = new Waiting(this, map, turn, new Markings(map));
    }

    public ImmutableCollection<Action> getActions() {
        // Don't expose actions since they can change state
        if (animationCounter.isAnimating()) {
            return ImmutableList.of();
        }
        return state.getActions();
    }

    public void select(Player player) {
        if (animationCounter.isAnimating()) return;
        state.select(player);
    }

    public void select(Enemy enemy) {
        if (animationCounter.isAnimating()) return;
        state.select(enemy);
    }

    public void select(Terrain terrain) {
        if (animationCounter.isAnimating()) return;
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
