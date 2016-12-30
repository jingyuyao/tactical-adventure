package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.state.AbstractState;
import com.jingyuyao.tactical.model.state.ChoosingItem;

public class ChooseItemToUse extends AbstractAction {
    private final Player player;

    public ChooseItemToUse(AbstractState state, Player player) {
        super(state);
        this.player = player;
    }

    @Override
    public String getName() {
        return "use item";
    }

    // TODO: Provider for state?
    @Override
    public void run() {
        getState().goTo(new ChoosingItem(getState(), player));
    }
}
