package com.jingyuyao.tactical.model.state;

public class FinishAction extends StateAction {
    FinishAction(State state) {
        super(state);
    }

    @Override
    public String getName() {
        return "Finish";
    }

    @Override
    public void run() {
        getState().transitionTo(new Waiting(getState()));
    }
}
