package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Selections;

abstract class AbstractState implements SelectionState {
    private final Map map;
    private final Selections selections;

    AbstractState(AbstractState prevState) {
        this(prevState.getMap(), prevState.getSelections());
    }

    AbstractState(Map map, Selections selections) {
        this.map = map;
        this.selections = selections;
    }

    Map getMap() {
        return map;
    }

    Selections getSelections() {
        return selections;
    }
}
