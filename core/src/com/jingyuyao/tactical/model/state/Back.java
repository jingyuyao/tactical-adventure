package com.jingyuyao.tactical.model.state;

public class Back extends AbstractAction {
    Back(AbstractState state) {
        super(state);
    }

    @Override
    public String getName() {
        return "Back";
    }

    @Override
    public void run() {
        getState().back();
    }
}
