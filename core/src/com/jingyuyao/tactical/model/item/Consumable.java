package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;

/**
 * An {@link Item} that can be used by a {@link Player}.
 */
public abstract class Consumable extends Usable {
    Consumable(EventBus eventBus, String name, int usageLeft) {
        super(eventBus, name, usageLeft);
    }

    public void consume(Player user) {
        performConsumption(user);
        useOnce();
    }

    protected abstract void performConsumption(Player user);
}
