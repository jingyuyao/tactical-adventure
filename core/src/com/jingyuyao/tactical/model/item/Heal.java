package com.jingyuyao.tactical.model.item;

import com.jingyuyao.tactical.model.object.Player;

public class Heal extends Consumable {
    // TODO: Remove me
    private static final int AMOUNT = 10;

    public Heal(int id, String name, int usageLeft) {
        super(id, name, usageLeft);
    }

    @Override
    protected void performConsumption(Player user) {
        user.healBy(AMOUNT);
    }
}
