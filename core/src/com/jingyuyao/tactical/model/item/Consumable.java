package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.object.Player;

/**
 * An {@link Item} that can be used by a {@link Player}.
 */
public abstract class Consumable extends Usable {
    Consumable(int id, String name, int usageLeft) {
        super(id, name, usageLeft);
    }

    public void consume(Player user) {
        performConsumption(user);
        useOnce();
    }

    protected abstract void performConsumption(Player user);
}
