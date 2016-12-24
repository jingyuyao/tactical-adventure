package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.jingyuyao.tactical.model.*;

abstract class AbstractState {
    /**
     * Lets not expose this to children so the only way to change state is via {@link #goTo(AbstractState)}.
     */
    private final MapState mapState;
    private final Map map;
    private final Turn turn;
    private final AnimationCounter animationCounter;
    private final Markings markings;
    private final AbstractState prevState;

    /**
     * Creates a new state with all of previous state's data and set {@link #prevState} of the new state
     * to the old state.
     */
    AbstractState(AbstractState prevState) {
        this(
                prevState.mapState,
                prevState.map,
                prevState.turn,
                prevState.animationCounter,
                prevState.markings,
                prevState
        );
    }

    /**
     * Creates a new state with the given data and set {@link #prevState} to null.
     */
    AbstractState(MapState mapState, Map map, Turn turn, AnimationCounter animationCounter, Markings markings) {
        this(mapState, map, turn, animationCounter, markings,  null);
    }

    /**
     * Creates a new state with the given data.
     */
    private AbstractState(
            MapState mapState,
            Map map,
            Turn turn,
            AnimationCounter animationCounter,
            Markings markings,
            AbstractState prevState
    ) {
        this.map = map;
        this.mapState = mapState;
        this.turn = turn;
        this.animationCounter = animationCounter;
        this.markings = markings;
        this.prevState = prevState;
    }

    Map getMap() {
        return map;
    }

    Markings getMarkings() {
        return markings;
    }

    Turn getTurn() {
        return turn;
    }

    AnimationCounter getAnimationCounter() {
        return animationCounter;
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
     */
    void hardCancel() {
        AbstractState currentState = this;
        while (!Waiting.class.isInstance(currentState)) {
            currentState.back();
            currentState = currentState.prevState;
        }
    }

    /**
     * Finished acting on {@code player} and then go to a new waiting state.
     */
    void finish(Player player) {
        getTurn().acted(player);
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

    abstract ImmutableCollection<Action> getActions();
}
