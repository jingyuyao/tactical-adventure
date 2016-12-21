package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Map;

abstract class AbstractState implements State {
    private final Map map;
    private final StateData stateData;

    AbstractState(AbstractState prevState) {
        this(prevState.getMap(), prevState.getStateData());
    }

    AbstractState(Map map, StateData stateData) {
        this.map = map;
        this.stateData = stateData;
    }

    Map getMap() {
        return map;
    }

    StateData getStateData() {
        return stateData;
    }
}
