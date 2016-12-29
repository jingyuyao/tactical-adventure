package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.util.DisposableObject;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState extends DisposableObject {
    private final Waiter waiter;
    private final State initialState;
    private State state;

    // TODO: create an annotation for the initial state
    public MapState(EventBus eventBus, Waiter waiter, State state) {
        super(eventBus);
        this.waiter = waiter;
        // TODO: add something like MapState.begin() so we can fire off a state change event to the view
        this.initialState = state;
        this.state = state;
    }

    @Override
    protected void disposed() {
        state = initialState;
        super.disposed();
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
