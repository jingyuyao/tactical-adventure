package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.object.Player;
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

    @Override
    public void run() {
        getState().goTo(new ChoosingItem(getState(), player));
    }
}
