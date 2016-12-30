package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status.
 * Not a {@link Consumable} since the effect of a {@link Weapon} depends on its user.
 */
public class Weapon extends Usable {
    private final int attackPower;
    private final ImmutableSet<Integer> attackDistances;

    Weapon(EventBus eventBus, int id, String name, int usageLeft, int attackPower, Iterable<Integer> attackDistances) {
        super(eventBus, id, name, usageLeft);
        this.attackPower = attackPower;
        this.attackDistances = ImmutableSet.copyOf(attackDistances);
    }

    public ImmutableSet<Integer> getAttackDistances() {
        return attackDistances;
    }

    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "attackPower=" + attackPower +
                ", attackDistances=" + attackDistances +
                "} " + super.toString();
    }
}
