package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.state.State;

public class StateChange implements ModelEvent {
    private final State newState;

    public StateChange(State newState) {
        this.newState = newState;
    }

    public State getNewState() {
        return newState;
    }
}
