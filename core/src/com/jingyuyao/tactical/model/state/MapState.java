package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
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
        state = new Waiting(this, map, turn, new Markings(map, animationCounter));
    }

    public ImmutableCollection<Action> getActions() {
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
            state.exit();
            newState.enter();
            state = newState;

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
        private final ImmutableCollection<Action> actions;

        StateChange(String stateName, ImmutableCollection<Action> actions) {
            this.stateName = stateName;
            this.actions = actions;
        }

        public String getStateName() {
            return stateName;
        }

        public ImmutableCollection<Action> getActions() {
            return actions;
        }
    }

    public static class ShowAttackPlan {
        private final AttackPlan attackPlan;

        ShowAttackPlan(AttackPlan attackPlan) {
            this.attackPlan = attackPlan;
        }

        public AttackPlan getAttackPlan() {
            return attackPlan;
        }
    }

    public static class HideAttackPlan {
        HideAttackPlan() {}
    }
}
