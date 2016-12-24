package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Player;

public class Done extends AbstractAction {
    private final Player currentPlayer;

    Done(AbstractState state, Player currentPlayer) {
        super(state);
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getName() {
        return "Done";
    }

    @Override
    public void run() {
        getState().done(currentPlayer);
    }
}
