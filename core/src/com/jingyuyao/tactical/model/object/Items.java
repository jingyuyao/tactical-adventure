package com.jingyuyao.tactical.model.object;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Targetable;
import com.jingyuyao.tactical.model.item.Usable;
import com.jingyuyao.tactical.model.item.Weapon;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A container for character items.
 * Belongs in object package since it is technically part of {@link Character}.
 * <br>
 * Invariants: all {@link Usable} objects must be removed immediately once {@link Usable#getUsageLeft()} == 0
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
        this(weapons, targetables, getDefaultWeapon(weapons));
    }

    public Items(List<Weapon> weapons, List<Targetable> targetables, Weapon equippedWeapon) {
        this.weapons = weapons;
        this.targetables = targetables;
        setEquippedWeapon(equippedWeapon);
        for (Item item : getItems()) {
            item.addObserver(this);
        }
    }

    Iterable<Weapon> getWeapons() {
        return weapons;
    }

    Iterable<Targetable> getTargetables() {
        return targetables;
    }

    Iterable<Item> getItems() {
        return Iterables.<Item>concat(weapons, targetables);
    }

    Optional<Weapon> getEquippedWeapon() {
        return Optional.fromNullable(equippedWeapon);
    }

    void setEquippedWeapon(Weapon weapon) {
        if (weapon != null) {
            Preconditions.checkArgument(weapons.contains(weapon));
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
                ", targetables=" + targetables +
                ", equippedWeapon=" + equippedWeapon +
                '}';
    }
}
