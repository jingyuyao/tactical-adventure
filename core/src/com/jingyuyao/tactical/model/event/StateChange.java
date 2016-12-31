package com.jingyuyao.tactical.model.event;

import com.jingyuyao.tactical.model.state.State;

public class StateChange extends ObjectEvent<State> {
    public StateChange(State newState) {
        super(newState);
    }
}
