package com.jingyuyao.tactical.model.object;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Targetable;
import com.jingyuyao.tactical.model.item.Weapon;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A container for character items.
 * Belongs in object package since it is technically part of {@link Character}.
 * Setters should be package private.
 */
public class Items implements Observer {
    private final List<Weapon> weapons;
    private final List<Targetable> targetables;
    /**
     * If not null, {@code weapons.contains(equippedWeapon) == true}
     */
    private Weapon equippedWeapon;

    /**
     * Creates an {@link Items} with the first weapon in {@code weapons} as the equipped weapon if it has one.
     */
    public Items(List<Weapon> weapons, List<Targetable> targetables) {
        this(weapons, targetables, weapons.isEmpty() ? null : weapons.get(0));
    }

    public Items(List<Weapon> weapons, List<Targetable> targetables, Weapon equippedWeapon) {
        this.weapons = weapons;
        this.targetables = targetables;
        setEquippedWeapon(equippedWeapon);
        for (Item item : getItems()) {
            item.addObserver(this);
        }
    }

    public Iterable<Weapon> getWeapons() {
        return Iterables.unmodifiableIterable(weapons);
    }

    public Iterable<Targetable> getTargetables() {
        return Iterables.unmodifiableIterable(targetables);
    }

    public Iterable<Item> getItems() {
        return Iterables.unmodifiableIterable(Iterables.<Item>concat(weapons, targetables));
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    void setEquippedWeapon(Weapon weapon) {
        if (weapon != null) {
            Preconditions.checkArgument(weapons.contains(weapon));
        }
        this.equippedWeapon = weapon;
    }

    @Override
    public void update(Observable observable, Object o) {
        // TODO: handle weapon used & broken
    }

    @Override
    public String toString() {
        return "Items{" +
                "weapons=" + weapons +
                ", targetables=" + targetables +
                ", equippedWeapon=" + equippedWeapon +
                '}';
    }
}
