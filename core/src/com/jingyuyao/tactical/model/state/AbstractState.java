package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Map;

abstract class AbstractState implements State {
    private final Map map;
    private final Markings markings;

    AbstractState(AbstractState prevState) {
        this(prevState.getMap(), prevState.getMarkings());
    }

    AbstractState(Map map, Markings markings) {
        this.map = map;
        this.markings = markings;
    }

    Map getMap() {
        return map;
    }

    Markings getMarkings() {
        return markings;
    }
}
