package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.object.Player;

public class UseConsumable extends AbstractAction {
    private final Consumable consumable;
    private final Player user;

    UseConsumable(AbstractState state, Consumable consumable, Player user) {
        super(state);
        this.consumable = consumable;
        this.user = user;
    }

    @Override
    public String getName() {
        return consumable.getName();
    }

    @Override
    public void run() {
        // TODO: kick off animation?
        consumable.consume(user);
        getState().wait(user);
    }
}
