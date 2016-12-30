package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.object.Items;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ItemFactory {
    private final EventBus eventBus;

    @Inject
    ItemFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Weapon createWeapon(int id, String name, int usageLeft, int attackPower, Iterable<Integer> attackDistances) {
        return new Weapon(eventBus, id, name, usageLeft, attackPower, attackDistances);
    }

    public Heal createHeal(int id, String name, int usageLeft) {
        return new Heal(eventBus, id, name, usageLeft);
    }

    public Items createItems(List<Weapon> weapons, List<Consumable> consumables) {
        return new Items(eventBus, weapons, consumables);
    }
}
