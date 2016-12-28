package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

import java.util.Observable;

/**
 * Manages selection logic.
 */
// TODO: This class needs to be thoroughly tested
public class MapState extends Observable {
    private final Waiter waiter;
    private AbstractState state;

    public MapState(Waiter waiter, Turn turn, MarkingFactory markingFactory, TargetInfo.Factory targetInfoFactory, AttackPlan.Factory attackPlanFactory) {
        this.waiter = waiter;
        state = new Waiting(this, turn, markingFactory, targetInfoFactory, attackPlanFactory);
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

            setChanged();
            notifyObservers(new StateChange(state.getClass().getSimpleName(), state.getActions()));
        }
    }

    void showAttackPlan(AttackPlan attackPlan) {
        setChanged();
        notifyObservers(new ShowAttackPlan(attackPlan));
    }

    void hideAttackPlan() {
        setChanged();
        notifyObservers(new HideAttackPlan());
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

    public static class ShowAttackPlan {
        private final AttackPlan attackPlan;

        private ShowAttackPlan(AttackPlan attackPlan) {
            this.attackPlan = attackPlan;
        }

        public AttackPlan getAttackPlan() {
            return attackPlan;
        }
    }

    public static class HideAttackPlan {
        private HideAttackPlan() {}
    }
}
