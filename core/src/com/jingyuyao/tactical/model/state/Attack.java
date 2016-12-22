package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Player;

public class Attack extends AbstractAction {
    private final Player currentPlayer;

    Attack(AbstractState state, Player currentPlayer) {
        super(state);
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String getName() {
        return "Attack";
    }

    @Override
    public void run() {
        getState().goTo(new Targeting(getState(), currentPlayer));
    }
}
