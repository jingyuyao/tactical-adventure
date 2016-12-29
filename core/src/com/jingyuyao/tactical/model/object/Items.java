package com.jingyuyao.tactical.model.object;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Usable;
import com.jingyuyao.tactical.model.item.Weapon;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A container for character items.
 * Belongs in object package since it is technically part of {@link Character}.
 * <br>
 * Invariants: all {@link Usable} objects must be removed immediately once {@link Usable#getUsageLeft()} == 0
 */
// TODO: test the invariant
public class Items implements Observer {
    private final EventBus eventBus;
    /**
     * Invariant: weapons.indexOf(equippedWeapon) == 0
     */
    private final List<Weapon> weapons;
    private final List<Consumable> consumables;
    /**
     * If not null, {@code weapons.contains(equippedWeapon) == true}
     */
    private Weapon equippedWeapon;

    /**
     * Creates an {@link Items} with the first weapon in {@code weapons} as the equipped weapon if it has one.
     */
    public Items(EventBus eventBus, List<Weapon> weapons, List<Consumable> consumables) {
        this(eventBus, weapons, consumables, getDefaultWeapon(weapons));
    }

    public Items(EventBus eventBus, List<Weapon> weapons, List<Consumable> consumables, Weapon equippedWeapon) {
        this.eventBus = eventBus;
        this.weapons = weapons;
        this.consumables = consumables;
        setEquippedWeapon(equippedWeapon);
        for (Item item : getItems()) {
            item.addObserver(this);
        }
    }

    Iterable<Weapon> getWeapons() {
        return weapons;
    }

    Iterable<Consumable> getConsumables() {
        return consumables;
    }

    Iterable<Item> getItems() {
        return Iterables.<Item>concat(weapons, consumables);
    }

    Optional<Weapon> getEquippedWeapon() {
        return Optional.fromNullable(equippedWeapon);
    }

    /**
     * Sets {@link #equippedWeapon} to {@code weapon}. Also swap {@code weapon} to the first
     * item in {@link #weapons} as per invariant.
     */
    void setEquippedWeapon(Weapon weapon) {
        if (weapon != null) {
            int weaponIndex = weapons.indexOf(weapon);
            Preconditions.checkArgument(weaponIndex != -1);
            // Previous check also guarantees weapons is not empty
            // Preserves the invariant
            Collections.swap(weapons, 0, weaponIndex);
        }
        this.equippedWeapon = weapon;
    }

    @Override
    public void update(Observable item, Object param) {
        if (Usable.Broke.class.isInstance(param)) {
            Iterables.removeIf(getItems(), Predicates.equalTo(item));
            if (item.equals(equippedWeapon)) {
                setEquippedWeapon(getDefaultWeapon(weapons));
            }
        }
    }

    /**
     * Return the first weapon if there is one, else null.
     */
    private static Weapon getDefaultWeapon(Iterable<Weapon> weapons) {
        return Iterables.getFirst(weapons, null);
    }

    @Override
    public String toString() {
        return "Items{" +
                "weapons=" + weapons +
                ", consumables=" + consumables +
                ", equippedWeapon=" + equippedWeapon +
                '}';
    }
}
