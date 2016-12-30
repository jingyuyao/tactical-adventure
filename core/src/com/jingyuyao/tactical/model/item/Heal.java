package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.object.Player;

public class Heal extends Consumable {
    // TODO: Remove me
    private static final int AMOUNT = 10;

    Heal(EventBus eventBus, int id, String name, int usageLeft) {
        super(eventBus, id, name, usageLeft);
    }

    @Override
    protected void performConsumption(Player user) {
        user.healBy(AMOUNT);
    }
}
