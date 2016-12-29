package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.state.AbstractState;

import java.util.Locale;

public class UseConsumable extends AbstractAction {
    private final Consumable consumable;
    private final Player user;

    public UseConsumable(AbstractState state, Consumable consumable, Player user) {
        super(state);
        this.consumable = consumable;
        this.user = user;
    }

    @Override
    public String getName() {
        return String.format(Locale.US, "%s (%d)", consumable.getName(), consumable.getUsageLeft());
    }

    @Override
    public void run() {
        // TODO: kick off animation?
        consumable.consume(user);
        getState().finish(user);
    }
}
