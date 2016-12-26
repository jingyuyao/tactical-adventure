package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Turn;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

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
        state = new Waiting(this, map, turn);
    }

    public ImmutableList<Action> getActions() {
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

    /**
     * Changes state to {@code newState} if it is not null.
     */
    void changeStateTo(AbstractState newState) {
        if (newState != null) {
            // TODO: add a test for the order of these events
            state.exit();

            state = newState;
            setChanged();
            notifyObservers(new StateChange(state.getClass().getSimpleName(), state.getActions()));

            newState.enter();
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
