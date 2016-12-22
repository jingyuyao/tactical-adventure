package com.jingyuyao.tactical.model.state;

public class Cancel extends AbstractAction {
    Cancel(AbstractState state) {
        super(state);
    }

    @Override
    public String getName() {
        return "Cancel";
    }

    @Override
    public void run() {
        getState().goToPrevState();
    }
}
