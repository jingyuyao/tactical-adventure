package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableSet;

public class Weapon extends Item {
    private final ImmutableSet<Integer> attackDistances;

    public Weapon(String name, int usageLeft, ImmutableSet<Integer> attackDistances) {
        super(name, usageLeft);
        this.attackDistances = attackDistances;
    }

    public ImmutableSet<Integer> getAttackDistances() {
        return attackDistances;
    }
}
