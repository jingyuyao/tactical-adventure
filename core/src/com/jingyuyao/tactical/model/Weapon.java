package com.jingyuyao.tactical.model;

import com.google.common.collect.ImmutableList;

public class Weapon {
    private final ImmutableList<Integer> attackDistances;
    private final String name;

    public Weapon(String name, ImmutableList<Integer> attackDistances) {
        this.name = name;
        this.attackDistances = attackDistances;
    }

    public ImmutableList<Integer> getAttackDistances() {
        return attackDistances;
    }

    public String getName() {
        return name;
    }
}
