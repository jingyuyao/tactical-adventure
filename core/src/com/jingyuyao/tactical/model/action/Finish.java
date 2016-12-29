package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.state.AbstractState;

public class Finish extends AbstractAction {
    private final Player currentPlayer;

    public Finish(AbstractState state, Player currentPlayer) {
        super(state);
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getName() {
        return "finish";
    }

    @Override
    public void run() {
        getState().finish(currentPlayer);
    }
}
