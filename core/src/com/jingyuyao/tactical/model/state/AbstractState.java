package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.jingyuyao.tactical.model.*;

abstract class AbstractState {
    private final Map map;
    private final MapState mapState;
    private final Turn turn;
    private final Markings markings;
    private final AbstractState prevState;

    AbstractState(AbstractState prevState) {
        this(prevState.mapState, prevState.map, prevState.turn, prevState.markings, prevState);
    }

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
        mapState.changeState(newState);
    }

    void goToPrevState() {
        // TODO: retrace path or let moving state handle it?
        goTo(prevState);
    }

    /**
     * Called when entering this state.
     *
     * Note: Constructor is unreliable for setting up new state data since we can go back to the previous state
     * which won't be re-instantiated.
     */
    abstract void enter();

    abstract void select(Player player);

    abstract void select(Enemy enemy);

    abstract void select(Terrain terrain);

    abstract ImmutableCollection<Action> getActions();
}
