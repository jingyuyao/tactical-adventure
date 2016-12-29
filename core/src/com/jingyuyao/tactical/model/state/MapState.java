package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.MarkingFactory;
import com.jingyuyao.tactical.model.TargetInfo;
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

    public MapState(EventBus eventBus, Waiter waiter, MarkingFactory markingFactory, TargetInfo.Factory targetInfoFactory, AttackPlan.Factory attackPlanFactory) {
        this.waiter = waiter;
        eventBus.register(this);
        // TODO: add something like MapState.begin() so we can fire off a state change event to the view
        state = new Waiting(eventBus, new Markings(eventBus, markingFactory), targetInfoFactory, attackPlanFactory);
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
