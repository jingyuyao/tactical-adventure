package com.jingyuyao.tactical.model.item;

import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.object.Character;

/**
 * An {@link Item} that can affect a {@link Character}'s HP and status.
 * Not a {@link Targetable} since the effect of a {@link Weapon} depends on its user.
 */
// TODO: do we need to add a common class for usable/targetable that depends on its owner?
public class Weapon extends Usable {
    private final int attackPower;
    private final ImmutableSet<Integer> attackDistances;

    public Weapon(int id, String name, int usageLeft, int attackPower, Iterable<Integer> attackDistances) {
        super(id, name, usageLeft);
        this.attackPower = attackPower;
        this.attackDistances = ImmutableSet.copyOf(attackDistances);
    }

    @Override
    protected void used() {}

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
