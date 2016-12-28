package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.TargetInfoFactory;
import com.jingyuyao.tactical.model.Turn;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

abstract class AbstractState {
    private final AbstractState prevState;
    /**
     * Lets not expose this to children so the only way to change state is via {@link #goTo(AbstractState)}.
     */
    private final MapState mapState;
    private final Map map;
    private final Turn turn;
    private final TargetInfoFactory targetInfoFactory;
    private final StateMarkings stateMarkings;

    /**
     * Creates a new state with all of previous state's data and set {@link #prevState} of the new state
     * to the old state.
     */
    AbstractState(AbstractState prevState) {
        this(prevState, prevState.map, prevState.mapState, prevState.turn, prevState.targetInfoFactory, prevState.stateMarkings);
    }

    /**
     * Creates a new state with the given data and set {@link #prevState} to null.
     */
    AbstractState(Map map, MapState mapState, Turn turn, TargetInfoFactory targetInfoFactory, StateMarkings stateMarkings) {
        this(null, map, mapState, turn, targetInfoFactory, stateMarkings);
    }

    /**
     * Creates a new state with the given data.
     */
    private AbstractState(AbstractState prevState, Map map, MapState mapState, Turn turn, TargetInfoFactory targetInfoFactory, StateMarkings stateMarkings) {
        this.prevState = prevState;
        this.map = map;
        this.mapState = mapState;
        this.turn = turn;
        this.targetInfoFactory = targetInfoFactory;
        this.stateMarkings = stateMarkings;
    }

    Map getMap() {
        return map;
    }

    TargetInfoFactory getTargetInfoFactory() {
        return targetInfoFactory;
    }

    StateMarkings getStateMarkings() {
        return stateMarkings;
    }

    void nextTurn() {
        turn.nextTurn();
    }

    void showAttackPlan(AttackPlan attackPlan) {
        mapState.showAttackPlan(attackPlan);
    }

    void hideAttackPlan() {
        mapState.hideAttackPlan();
    }

    void goTo(AbstractState newState) {
        mapState.changeStateTo(newState);
    }

    /**
     * Cancel {@link #prevState} then {@link #goTo(AbstractState)} it
     */
    void back() {
        prevState.canceled();
        goTo(prevState);
    }

    /**
     * Go {@link #back()} state by state until we reach a {@link Waiting} state.
     * The function of this method depends on {@link Waiting#backToWaiting()} returning itself.
     * @return The {@link Waiting} we went back to, useful for "reset then go to a new state" scenario
     */
    Waiting backToWaiting() {
        // Will recursively loop until we encounter a waiting state
        back();
        return prevState.backToWaiting();
    }

    /**
     * Finished acting on {@code player} and then go to a new waiting state.
     */
    void wait(Player player) {
        player.setActionable(false);
        goTo(new Waiting(this));
    }

    /**
     * Called when entering this state.
     *
     * Note: Constructor is unreliable for setting up new state data since we can go back to the previous state
     * which won't be re-instantiated.
     */
    abstract void enter();

    /**
     * Called when this state is canceled by going to previous state.
     * Do NOT change state in this method.
     */
    abstract void canceled();

    /**
     * Called when this state exits.
     */
    abstract void exit();

    abstract void select(Player player);

    abstract void select(Enemy enemy);

    abstract void select(Terrain terrain);

    abstract ImmutableList<Action> getActions();
}
