package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Player;

public class Finish extends AbstractAction {
    private final Player currentPlayer;

    Finish(AbstractState state, Player currentPlayer) {
        super(state);
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getName() {
        return "Finish";
    }

    @Override
    public void run() {
        getState().finish(currentPlayer);
    }
}
