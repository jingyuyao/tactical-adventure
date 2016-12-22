package com.jingyuyao.tactical.model.state;

abstract class AbstractAction implements Action {
    private final AbstractState state;

    AbstractAction(AbstractState state) {
        this.state = state;
    }

    public AbstractState getState() {
        return state;
    }
}
