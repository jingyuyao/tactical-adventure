package com.jingyuyao.tactical.model.state;

public class EndTurn extends AbstractAction {
    EndTurn(AbstractState state) {
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
