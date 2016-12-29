package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;

import javax.inject.Singleton;

@Singleton
public class ItemFactory {
    private final EventBus eventBus;

    @Singleton
    ItemFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Weapon createWeapon(int id, String name, int usageLeft, int attackPower, Iterable<Integer> attackDistances) {
        return new Weapon(eventBus, id, name, usageLeft, attackPower, attackDistances);
    }

    public Heal createHeal(int id, String name, int usageLeft) {
        return new Heal(eventBus, id, name, usageLeft);
    }
}
