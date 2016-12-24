package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Player;

public class Wait extends AbstractAction {
    private final Player currentPlayer;

    Wait(AbstractState state, Player currentPlayer) {
        super(state);
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getName() {
        return "wait";
    }

    @Override
    public void run() {
        getState().wait(currentPlayer);
    }
}
