package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.jingyuyao.tactical.model.*;

abstract class AbstractState {
    private final Map map;
    /**
     * Lets not expose this to children so the only way to change state is via {@link #goTo(AbstractState)}.
     */
    private final MapState mapState;
    private final Turn turn;
    private final Markings markings;
    private final AbstractState prevState;

    /**
     * Creates a new state with all of previous state's data and set {@link #prevState} of the new state
     * to the old state.
     */
    AbstractState(AbstractState prevState) {
        this(prevState.mapState, prevState.map, prevState.turn, prevState.markings, prevState);
    }

    /**
     * Creates a new state with the given data and set {@link #prevState} to null.
     */
    AbstractState(MapState mapState, Map map, Turn turn, Markings markings) {
        this(mapState, map, turn, markings, null);
    }

    /**
     * Creates a new state with the given data.
     */
    AbstractState(MapState mapState, Map map, Turn turn, Markings markings, AbstractState prevState) {
        this.map = map;
        this.mapState = mapState;
        this.turn = turn;
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

    void goTo(AbstractState newState) {
        mapState.changeStateTo(newState);
    }

    void goToPrevState() {
        canceled();
        goTo(prevState);
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
