package com.jingyuyao.tactical.model.state;

public class StateChange {
    private final State newState;

    StateChange(State newState) {
        this.newState = newState;
    }

    public State getNewState() {
        return newState;
    }
}
