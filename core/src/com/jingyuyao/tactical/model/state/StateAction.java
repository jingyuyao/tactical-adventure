package com.jingyuyao.tactical.model.state;

public abstract class StateAction {
    private final State state;

    StateAction(State state) {
        this.state = state;
    }

    public abstract String getName();

    public abstract void run();

    public State getState() {
        return state;
    }
}
