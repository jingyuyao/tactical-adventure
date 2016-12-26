package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.object.Player;

public class ChooseItemToUse extends AbstractAction {
    private final Player player;

    ChooseItemToUse(AbstractState state, Player player) {
        super(state);
        this.player = player;
    }

    @Override
    public String getName() {
        return "use item";
    }

    @Override
    public void run() {
        getState().goTo(new ChoosingItem(getState(), player));
    }
}
