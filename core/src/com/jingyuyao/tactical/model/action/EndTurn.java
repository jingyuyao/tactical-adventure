package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.state.AbstractState;

public class EndTurn extends AbstractAction {
    public EndTurn(AbstractState state) {
        super(state);
    }

    @Override
    public String getName() {
        return "End Turn";
    }

    @Override
    public void run() {
        getState().nextTurn();
    }
}
