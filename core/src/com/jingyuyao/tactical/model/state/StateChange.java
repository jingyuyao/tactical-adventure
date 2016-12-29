package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.util.ModelEvent;

public class StateChange implements ModelEvent {
    private final State newState;

    StateChange(State newState) {
        this.newState = newState;
    }

    public State getNewState() {
        return newState;
    }
}
