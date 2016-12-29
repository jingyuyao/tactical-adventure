package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState {
    private final EventBus eventBus;
    private final Waiter waiter;
    private AbstractState state;

    public MapState(EventBus eventBus, Waiter waiter, Turn turn, MarkingFactory markingFactory, TargetInfo.Factory targetInfoFactory, AttackPlan.Factory attackPlanFactory) {
        this.eventBus = eventBus;
        this.waiter = waiter;
        // TODO: add something like MapState.begin() so we can fire off a state change event to the view
        state = new Waiting(eventBus, this, turn, markingFactory, targetInfoFactory, attackPlanFactory);
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

    /**
     * Changes state to {@code newState} if it is not null.
     */
    void changeStateTo(AbstractState newState) {
        if (newState != null) {
            state.exit();
            state = newState;
            state.enter();

            eventBus.post(new StateChange(state.getClass().getSimpleName(), state.getActions()));
        }
    }

    public static class StateChange {
        private final String stateName;
        private final ImmutableList<Action> actions;

        private StateChange(String stateName, Iterable<Action> actions) {
            this.stateName = stateName;
            this.actions = ImmutableList.copyOf(actions);
        }

        public String getStateName() {
            return stateName;
        }

        public ImmutableList<Action> getActions() {
            return actions;
        }
    }
}
