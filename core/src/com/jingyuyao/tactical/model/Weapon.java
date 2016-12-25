package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableSet;

public class Weapon extends Item {
    private final ImmutableSet<Integer> attackDistances;

    public Weapon(String name, int usageLeft, Iterable<Integer> attackDistances) {
        super(name, usageLeft);
        this.attackDistances = ImmutableSet.copyOf(attackDistances);
    }

    public ImmutableSet<Integer> getAttackDistances() {
        return attackDistances;
    }
}
