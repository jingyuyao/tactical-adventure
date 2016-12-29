package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.state.AbstractState;

public class Back extends AbstractAction {
    public Back(AbstractState state) {
        super(state);
    }

    @Override
    public String getName() {
        return "back";
    }

    @Override
    public void run() {
        getState().back();
    }
}
