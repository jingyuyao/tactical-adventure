package com.jingyuyao.tactical.model.item;

import com.google.common.collect.Iterables;

import java.util.List;

/**
 * A container for character items
 */
public class Items {
    private final List<Weapon> weapons;
    private final List<Consumable> consumables;

    public Items(List<Weapon> weapons, List<Consumable> consumables) {
        this.weapons = weapons;
        this.consumables = consumables;
    }

    public Iterable<Weapon> getWeapons() {
        return Iterables.unmodifiableIterable(weapons);
    }

    public Iterable<Consumable> getConsumbles() {
        return Iterables.unmodifiableIterable(consumables);
    }

    public Iterable<Item> getItems() {
        return Iterables.unmodifiableIterable(Iterables.concat(weapons, consumables));
    }
}
