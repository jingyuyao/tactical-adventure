package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;
import java.util.Collection;

abstract class State {
    private final Map map;
    private final MapState mapState;
    private final Markings markings;
    private final Collection<StateAction> stateActions;

    State(State prevState) {
        this(prevState.map, prevState.mapState, prevState.markings);
    }

    State(Map map, MapState mapState, Markings markings) {
        this.map = map;
        this.mapState = mapState;
        this.markings = markings;
        this.stateActions = new ArrayList<StateAction>();
    }

    Collection<StateAction> getStateActions() {
        return stateActions;
    }

    Map getMap() {
        return map;
    }

    Markings getMarkings() {
        return markings;
    }

    void transitionTo(State newState) {
        mapState.changeState(newState);
    }

    abstract State select(Player player);

    abstract State select(Enemy enemy);

    abstract State select(Terrain terrain);
}
