package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState {
    private final Waiter waiter;
    private State state;

    // TODO: create an annotation for the initial state
    public MapState(EventBus eventBus, Waiter waiter, State state) {
        eventBus.register(this);
        this.waiter = waiter;
        // TODO: add something like MapState.begin() so we can fire off a state change event to the view
        this.state = state;
    }

    public ImmutableList<Action> getActions() {
        return state.getActions();
    }

    public void select(Player player) {
        if (waiter.isWaiting()) return;
        state.select(player);
    }

    public void select(Enemy enemy) {
        if (waiter.isWaiting()) return;
        state.select(enemy);
    }

    public void select(Terrain terrain) {
        if (waiter.isWaiting()) return;
        state.select(terrain);
    }

    @Subscribe
    public void stateChange(StateChange stateChange) {
        state.exit();
        state = stateChange.getNewState();
        state.enter();
    }
}
