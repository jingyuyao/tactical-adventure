package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.state.AbstractState;

abstract class AbstractAction implements Action {
    private final AbstractState state;

    AbstractAction(AbstractState state) {
        this.state = state;
    }

    AbstractState getState() {
        return state;
    }
}
